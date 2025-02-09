package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto create(NewDeliveryDto newDeliveryDto);

    void setDeliverySuccess(UUID deliveryId);

    void setDeliveryPicked(UUID deliveryId);

    void setDeliveryFailed(UUID deliveryId);

    double calculateDeliveryCost(OrderDto orderDto);
}