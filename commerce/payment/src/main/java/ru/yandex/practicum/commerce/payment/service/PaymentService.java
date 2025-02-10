package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto create(OrderDto orderDto);

    double calculateTotalCost(OrderDto orderDto);

    void setPaymentSuccess(UUID paymentId);

    double calculateProductCost(OrderDto orderDto);

    void setPaymentFailed(UUID paymentId);
}