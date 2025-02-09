package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    DeliveryDto create(@Valid @RequestBody NewDeliveryDto newDeliveryDto);

    @PostMapping("/successful")
    void setDeliverySuccess(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    void setDeliveryPicked(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    void setDeliveryFailed(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    double calculateDeliveryCost(@Valid @RequestBody OrderDto orderDto);
}
