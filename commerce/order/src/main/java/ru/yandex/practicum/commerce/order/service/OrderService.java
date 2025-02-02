package ru.yandex.practicum.commerce.order.service;

import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.order.ProductReturnRequestDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto create(String username, NewOrderDto newOrderDto);

    List<OrderDto> getByUsername(String username, PageableDto pageableDto);

    OrderDto returnOrder(ProductReturnRequestDto productReturnRequestDto);

    OrderDto setPaymentMade(UUID orderId);

    OrderDto setPaymentFailed(UUID orderId);

    OrderDto setDeliveryMade(UUID orderId);

    OrderDto setDeliveryFailed(UUID orderId);

    OrderDto setCompleted(UUID orderId);

    OrderDto calculateTotal(UUID orderId);

    OrderDto calculateDelivery(UUID orderId);

    OrderDto assembly(UUID orderId);

    OrderDto setAssemblyFailed(UUID orderId);
}