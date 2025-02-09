package ru.yandex.practicum.commerce.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.feignclient.DeliveryClient;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/delivery")
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @PutMapping
    @Override
    public DeliveryDto create(@Valid @RequestBody NewDeliveryDto newDeliveryDto) {
        log.trace("PUT /api/v1/delivery with NewDeliveryDto {}", newDeliveryDto);
        return deliveryService.create(newDeliveryDto);
    }

    @PostMapping("/successful")
    @Override
    public void setDeliverySuccess(@RequestBody UUID deliveryId) {
        log.trace("POST /api/v1/delivery/successful with deliveryId {}", deliveryId);
        deliveryService.setDeliverySuccess(deliveryId);
    }

    @PostMapping("/picked")
    @Override
    public void setDeliveryPicked(@RequestBody UUID deliveryId) {
        log.trace("POST /api/v1/delivery/picked with deliveryId {}", deliveryId);
        deliveryService.setDeliveryPicked(deliveryId);
    }

    @PostMapping("/failed")
    @Override
    public void setDeliveryFailed(@RequestBody UUID deliveryId) {
        log.trace("POST /api/v1/delivery/failed with deliveryId {}", deliveryId);
        deliveryService.setDeliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    @Override
    public double calculateDeliveryCost(@Valid @RequestBody OrderDto orderDto) {
        log.trace("POST /api/v1/delivery/cost with OrderDto {}", orderDto);
        return deliveryService.calculateDeliveryCost(orderDto);
    }
}