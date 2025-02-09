package ru.yandex.practicum.commerce.delivery.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.model.Delivery;

@Service
public class DeliveryCostCalculatorImpl implements DeliveryCostCalculator {
    private static final double BASIC_COST = 5.0;
    private static final String ADDRESS_FROM_NAME_TRIGGER = "ADDRESS_2";
    private static final double ADDRESS_FROM_COEFFICIENT = 2;
    private static final double FRAGILITY_COEFFICIENT = 1.2;
    private static final double WEIGHT_COEFFICIENT = 0.3;
    private static final double VOLUME_COEFFICIENT = 0.2;
    private static final double ADDRESS_TO_COEFFICIENT = 1.2;

    @Override
    public double calculateDeliveryCost(Delivery delivery) {
        double cost = BASIC_COST;

        cost *= ADDRESS_FROM_NAME_TRIGGER.equals(delivery.getFromAddress().getStreet()) ? ADDRESS_FROM_COEFFICIENT : 1;
        cost *= delivery.getFragile() ? FRAGILITY_COEFFICIENT : 1;
        cost += delivery.getDeliveryWeight() * WEIGHT_COEFFICIENT;
        cost += delivery.getDeliveryVolume() * VOLUME_COEFFICIENT;
        cost *= delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet()) ? 1 : ADDRESS_TO_COEFFICIENT;

        return cost;
    }
}