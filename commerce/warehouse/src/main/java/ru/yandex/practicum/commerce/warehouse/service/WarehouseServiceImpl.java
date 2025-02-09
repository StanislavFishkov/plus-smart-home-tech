package ru.yandex.practicum.commerce.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.*;
import ru.yandex.practicum.commerce.common.dto.AddressDto;
import ru.yandex.practicum.commerce.common.error.exception.ConflictDataException;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.error.exception.ValidationException;
import ru.yandex.practicum.commerce.warehouse.mapper.ProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.commerce.warehouse.repository.ProductRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductRepository productRepository;
    private final OrderBookingRepository orderBookingRepository;
    private final ProductMapper productMapper;

    private final AddressDto currentAddressDto;

    public WarehouseServiceImpl(ProductRepository productRepository, OrderBookingRepository orderBookingRepository,
                                ProductMapper productMapper) {
        final String[] addresses = new String[] {"ADDRESS_1", "ADDRESS_2"};
        final String address = addresses[Random.from(new SecureRandom()).nextInt(0, 1)];
        currentAddressDto = AddressDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();

        this.productRepository = productRepository;
        this.orderBookingRepository = orderBookingRepository;
        this.productMapper = productMapper;
    }

    @Override
    public AddressDto getAddress() {
        return currentAddressDto;
    }

    @Override
    @Transactional
    public void createProduct(NewProductDto newProductDto) {
        if (productRepository.existsById(newProductDto.getProductId()))
            throw new ValidationException("Product already exists with id " + newProductDto.getProductId());

        Product product = productRepository.save(productMapper.toEntity(newProductDto));
        log.info("Product is created: {} ", newProductDto);
    }

    @Override
    @Transactional
    public void addProduct(AddProductQuantityDto addProductQuantityDto) {
        Product product = productRepository.findById(addProductQuantityDto.getProductId())
                .orElseThrow(() -> new ValidationException("Product doesn't exists with id "
                        + addProductQuantityDto.getProductId()));

        product.setQuantity(product.getQuantity() + addProductQuantityDto.getQuantity());

        productRepository.save(product);
        log.info("Product quantity is added: {} ", addProductQuantityDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantities(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Integer> productsToCheck = shoppingCartDto.getProducts();
        Map<UUID, Product> products = getAndCheckProductsExistAtWarehouse(productsToCheck);

        checkEnoughQuantitiesAtWarehouse(products, productsToCheck);

        BookedProductsDto bookedProductsDto = getBookedProductsDto(products, productsToCheck);

        log.info("Product quantities checked for shopping cart: {} and shipping data calculated {}", shoppingCartDto,
                bookedProductsDto);
        return bookedProductsDto;
    }

    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequestDto shippedToDeliveryRequestDto) {
        OrderBooking orderBooking = checkAndGetOrderBookingById(shippedToDeliveryRequestDto.getOrderId());

        orderBooking.setDeliveryId(shippedToDeliveryRequestDto.getDeliveryId());
        orderBookingRepository.save(orderBooking);
        log.info("Order is shipped to delivery: {} ", shippedToDeliveryRequestDto);
    }

    @Override
    @Transactional
    public void returnProducts(Map<UUID, Integer> productsToReturn) {
        Map<UUID, Product> products = getAndCheckProductsExistAtWarehouse(productsToReturn);

        products.values().forEach(p -> p.setQuantity(p.getQuantity() + productsToReturn.get(p.getProductId())));
        productRepository.saveAll(products.values());
        log.info("Products are returned to the warehouse: {} ", productsToReturn);
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequestDto assemblyProductsForOrderRequestDto) {
        UUID orderId = assemblyProductsForOrderRequestDto.getOrderId();
        if (orderBookingRepository.existsById(orderId))
            throw new ConflictDataException("Booking already exists for order with id: %s".formatted(orderId));

        Map<UUID, Integer> productsToBook = assemblyProductsForOrderRequestDto.getProducts();
        Map<UUID, Product> products = getAndCheckProductsExistAtWarehouse(productsToBook);

        checkEnoughQuantitiesAtWarehouse(products, productsToBook);

        products.values().forEach(p -> p.setQuantity(p.getQuantity() - productsToBook.get(p.getProductId())));
        productRepository.saveAll(products.values());

        OrderBooking orderBooking = orderBookingRepository.save(OrderBooking.builder()
                .orderId(assemblyProductsForOrderRequestDto.getOrderId())
                .products(productsToBook)
                .build());

        BookedProductsDto bookedProductsDto = getBookedProductsDto(products, productsToBook);

        log.info("Products are assembled: {} and shipping data is calculated {}", assemblyProductsForOrderRequestDto,
                bookedProductsDto);
        return bookedProductsDto;
    }

    private OrderBooking checkAndGetOrderBookingById(UUID orderId) {
        return orderBookingRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Booking doesn't exist for order with id: %s".formatted(orderId)));
    }

    private Map<UUID, Product> getAndCheckProductsExistAtWarehouse(Map<UUID, Integer> productsToCheck) {
        Map<UUID, Product> products = productRepository.findAllById(productsToCheck.keySet()).stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        if (products.size() < productsToCheck.size())
            productsToCheck.keySet().stream()
                    .filter(productId -> !products.containsKey(productId))
                    .map("Product with id %s doesn't exist at warehouse"::formatted)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                    .filter(l -> !l.isEmpty())
                    .ifPresent(l -> {
                        throw new NotFoundException(l.toString());
                    });

        return products;
    }

    private void checkEnoughQuantitiesAtWarehouse(Map<UUID, Product> products, Map<UUID, Integer> productsToCheck) {
        // check if there is enough quantity at the warehouse
        products.values().stream()
                .filter(p -> p.getQuantity() < productsToCheck.get(p.getProductId()))
                .map(p -> "Product with id %s has low quantity %d".formatted(p.getProductId(), p.getQuantity()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .filter(l -> !l.isEmpty())
                .ifPresent(l -> {
                    throw new ConflictDataException(l.toString());
                });
    }

    private BookedProductsDto getBookedProductsDto(Map<UUID, Product> products, Map<UUID, Integer> productsToBook) {
        // collect shipping data
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        for (Product product : products.values()) {
            Integer quantityToBook = productsToBook.get(product.getProductId());

            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() + quantityToBook * product.getWeight());
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    quantityToBook * product.getDimension().getVolume());
            bookedProductsDto.setFragile(product.getFragile() || bookedProductsDto.getFragile());
        }

        return bookedProductsDto;
    }
}