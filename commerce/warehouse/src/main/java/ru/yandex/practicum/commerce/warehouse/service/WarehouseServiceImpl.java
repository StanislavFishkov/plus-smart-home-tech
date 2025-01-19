package ru.yandex.practicum.commerce.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.error.exception.ValidationException;
import ru.yandex.practicum.commerce.warehouse.mapper.ProductMapper;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.ProductRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final AddressDto currentAddressDto;

    public WarehouseServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
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
    @Transactional
    public BookedProductsDto checkProductQuantities(ShoppingCartDto shoppingCartDto) {
        var shoppingCartProducts = shoppingCartDto.getProducts();
        List<Product> products = productRepository.findAllById(shoppingCartProducts.keySet().stream().toList());

        // check if every product is present at the warehouse
        if (products.size() < shoppingCartDto.getProducts().size())
            shoppingCartProducts.keySet().stream()
                    .filter(productId -> products.stream().noneMatch(product -> product.getProductId().equals(productId)))
                    .map(productId -> String.format("Product with id %s was not found at Warehouse", productId))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                    .filter(l -> !l.isEmpty())
                    .ifPresent(l -> {
                        throw new NotFoundException(l.toString());
                    });

        // check if there is enough quantity at the warehouse
        products.stream()
                .filter(p -> p.getQuantity() < shoppingCartProducts.get(p.getProductId()))
                .map(p -> String.format("Product with id %s has low quantity %d", p.getProductId(), p.getQuantity()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .filter(l -> !l.isEmpty())
                .ifPresent(l -> {
                    throw new ValidationException(l.toString());
                });

        // collect shipping data
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        for (Product product : products) {
            Integer quantityToBook = shoppingCartProducts.get(product.getProductId());

            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() + quantityToBook * product.getWeight());
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    quantityToBook * product.getDimension().getVolume());
            bookedProductsDto.setFragile(product.getFragile() || bookedProductsDto.getFragile());
        }

        return bookedProductsDto;
    }
}
