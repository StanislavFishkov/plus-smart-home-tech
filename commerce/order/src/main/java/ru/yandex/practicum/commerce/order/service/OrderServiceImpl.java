package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.order.ProductReturnRequestDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.common.error.exception.ConflictDataException;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.feignclient.PaymentClient;
import ru.yandex.practicum.commerce.common.model.OrderState;
import ru.yandex.practicum.commerce.common.util.PagingUtil;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.model.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final PlatformTransactionManager platformTransactionManager;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    public OrderDto create(String username, NewOrderDto newOrderDto) {
        Order order = orderRepository.save(orderMapper.toEntity(username, newOrderDto));
        log.info("Order is created: {} with id {}", newOrderDto, order.getOrderId());
        return orderMapper.toDto(order);
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
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setReadOnly(true);
        OrderDto orderDto = transactionTemplate.execute(status -> {
            return orderMapper.toDto(checkAndGetOrderById(orderId));
        });

        if (orderDto == null || orderDto.getDeliveryPrice() == null)
            throw new ConflictDataException("Delivery price must be calculated before calculating total: %s".formatted(orderDto));

        orderDto.setProductPrice(paymentClient.calculateProductCost(orderDto));
        orderDto.setTotalPrice(paymentClient.calculateTotalCost(orderDto));

        PaymentDto paymentDto = paymentClient.create(orderDto);

        transactionTemplate = new TransactionTemplate(platformTransactionManager);
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
        return null;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        return null;
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