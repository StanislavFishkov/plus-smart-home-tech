package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.delivery.model.Delivery;

public interface DeliveryCostCalculator {
    double calculateDeliveryCost(Delivery delivery);
}