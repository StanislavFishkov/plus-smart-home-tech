package ru.yandex.practicum.commerce.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.order.ProductReturnRequestDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AssemblyProductsForOrderRequestDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.error.exception.ConflictDataException;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.feignclient.DeliveryClient;
import ru.yandex.practicum.commerce.common.feignclient.PaymentClient;
import ru.yandex.practicum.commerce.common.feignclient.WarehouseClient;
import ru.yandex.practicum.commerce.common.model.OrderState;
import ru.yandex.practicum.commerce.common.util.PagingUtil;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final TransactionTemplate transactionTemplateReadOnly;
    private final TransactionTemplate transactionTemplate;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper,
                            WarehouseClient warehouseClient, DeliveryClient deliveryClient, PaymentClient paymentClient,
                            PlatformTransactionManager platformTransactionManager) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.warehouseClient = warehouseClient;
        this.deliveryClient = deliveryClient;
        this.paymentClient = paymentClient;

        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.transactionTemplateReadOnly = new TransactionTemplate(platformTransactionManager);
        this.transactionTemplateReadOnly.setReadOnly(true);
    }

    @Override
    public OrderDto create(String username, NewOrderDto newOrderDto) {
        UUID orderId = transactionTemplate.execute(status -> {
            return orderRepository.save(orderMapper.toEntity(username, newOrderDto)).getOrderId();
        });

        DeliveryDto deliveryDto = deliveryClient.create(NewDeliveryDto.builder()
                .orderId(orderId)
                .fromAddress(warehouseClient.getAddress())
                .toAddress(newOrderDto.getDeliveryAddress())
                .build());

        OrderDto orderDto = transactionTemplate.execute(status -> {
            Order order = checkAndGetOrderById(orderId);
            order.setDeliveryId(deliveryDto.getDeliveryId());
            orderRepository.save(order);

            return orderMapper.toDto(order);
        });

        log.info("Order is created: {} with id {}", orderDto, orderId);
        return orderDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getByUsername(String username, PageableDto pageableDto) {
        log.info("Orders are requested by username {}", username);
        return orderMapper.toDto(orderRepository.findAllByUsername(username, PagingUtil.pageOf(pageableDto)));
    }

    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequestDto productReturnRequestDto) {
        Order order = checkAndGetOrderById(productReturnRequestDto.getOrderId());

        if (!order.getProducts().equals(productReturnRequestDto.getProducts()))
            throw new ConflictDataException("Returning products aren't equal to products in order %s".formatted(order.getOrderId()));

        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);
        log.info("Order has been returned: {}", order.getOrderId());
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto setPaymentMade(UUID orderId) {
        return changeOrderState(orderId, OrderState.PAID);
    }

    @Override
    @Transactional
    public OrderDto setPaymentFailed(UUID orderId) {
        return changeOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    @Transactional
    public OrderDto setDeliveryMade(UUID orderId) {
        return changeOrderState(orderId, OrderState.DELIVERED);
    }

    @Override
    @Transactional
    public OrderDto setDeliveryFailed(UUID orderId) {
        return changeOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    @Transactional
    public OrderDto setCompleted(UUID orderId) {
        return changeOrderState(orderId, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        OrderDto orderDto = Objects.requireNonNull(transactionTemplateReadOnly.execute(status -> {
            return orderMapper.toDto(checkAndGetOrderById(orderId));
        }));

        if (orderDto.getDeliveryPrice() == null)
            throw new ConflictDataException("Delivery price must be calculated before calculating total: %s".formatted(orderDto));

        orderDto.setProductPrice(paymentClient.calculateProductCost(orderDto));
        orderDto.setTotalPrice(paymentClient.calculateTotalCost(orderDto));

        PaymentDto paymentDto = paymentClient.create(orderDto);

        orderDto = transactionTemplate.execute(status -> {
            Order order = checkAndGetOrderById(orderId);
            order.setPaymentId(paymentDto.getPaymentId());
            order.setProductPrice(paymentDto.getTotalPayment() - paymentDto.getDeliveryTotal() - paymentDto.getFeeTotal());
            order.setTotalPrice(paymentDto.getTotalPayment());
            order.setState(OrderState.ON_PAYMENT);
            order = orderRepository.save(order);
            return orderMapper.toDto(order);
        });
        log.info("Payment is calculated and saved for order: {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        OrderDto orderDto = Objects.requireNonNull(transactionTemplateReadOnly.execute(status -> {
            return orderMapper.toDto(checkAndGetOrderById(orderId));
        }));
        double deliveryPrice = deliveryClient.calculateDeliveryCost(orderDto);

        orderDto = transactionTemplate.execute(status -> {
            Order order = checkAndGetOrderById(orderId);
            order.setDeliveryPrice(deliveryPrice);
            order.setState(OrderState.ON_DELIVERY);
            order = orderRepository.save(order);
            return orderMapper.toDto(order);
        });
        log.info("Delivery is calculated for order: {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        var products = Objects.requireNonNull(transactionTemplateReadOnly.execute(status -> {
            return checkAndGetOrderById(orderId).getProducts();
        }));

        AssemblyProductsForOrderRequestDto assemblyDto = AssemblyProductsForOrderRequestDto.builder()
                .orderId(orderId)
                .products(products)
                .build();

        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProductForOrderFromShoppingCart(assemblyDto);

        OrderDto orderDto = Objects.requireNonNull(transactionTemplate.execute(status -> {
            Order order = checkAndGetOrderById(orderId);
            order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
            order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
            order.setFragile(bookedProductsDto.getFragile());
            order.setState(OrderState.ASSEMBLED);
            orderRepository.save(order);
            return orderMapper.toDto(order);
        }));
        log.info("Order is assembled: {}", orderDto);
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto setAssemblyFailed(UUID orderId) {
        return changeOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private Order checkAndGetOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order doesn't exist with id: %s".formatted(orderId)));
    }

    private OrderDto changeOrderState(UUID orderId, OrderState newOrderState) {
        Order order = checkAndGetOrderById(orderId);
        order.setState(newOrderState);
        orderRepository.save(order);
        log.info("Order state has been changed to {}: {}", newOrderState, order.getOrderId());
        return orderMapper.toDto(order);
    }
}