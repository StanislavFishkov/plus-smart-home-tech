package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.order.ProductReturnRequestDto;
import ru.yandex.practicum.commerce.common.feignclient.OrderClient;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @PutMapping
    @Override
    public OrderDto create(@NotBlank @RequestParam("username") String username,
                           @Valid @RequestBody NewOrderDto newOrderDto) {
        log.trace("PUT /api/v1/order with username {} and NewOrderDto {}", username, newOrderDto);
        return orderService.create(username, newOrderDto);
    }

    @GetMapping
    @Override
    public List<OrderDto> get(@NotBlank @RequestParam("username") String username, @Valid PageableDto pageableDto) {
        log.trace("GET /api/v1/order with username {} and PageableDto {}", username, pageableDto);
        return orderService.getByUsername(username, pageableDto);
    }

    @PostMapping("/return")
    @Override
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequestDto productReturnRequestDto) {
        log.trace("POST /api/v1/order/return with ProductReturnRequestDto {}", productReturnRequestDto);
        return orderService.returnOrder(productReturnRequestDto);
    }

    @PostMapping("/payment")
    @Override
    public OrderDto setPaymentMade(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/payment with orderId {}", orderId);
        return orderService.setPaymentMade(orderId);
    }

    @PostMapping("/payment/failed")
    @Override
    public OrderDto setPaymentFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/payment/failed with orderId {}", orderId);
        return orderService.setPaymentFailed(orderId);
    }

    @PostMapping("/delivery")
    @Override
    public OrderDto setDeliveryMade(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/delivery with orderId {}", orderId);
        return orderService.setDeliveryMade(orderId);
    }

    @PostMapping("/delivery/failed")
    @Override
    public OrderDto setDeliveryFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/delivery/failed with orderId {}", orderId);
        return orderService.setDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    @Override
    public OrderDto setCompleted(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/completed with orderId {}", orderId);
        return orderService.setCompleted(orderId);
    }

    @PostMapping("/calculate/total")
    @Override
    public OrderDto calculateTotal(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/calculate/total with orderId {}", orderId);
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    @Override
    public OrderDto calculateDelivery(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/calculate/delivery with orderId {}", orderId);
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    @Override
    public OrderDto assembly(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/assembly with orderId {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    @Override
    public OrderDto setAssemblyFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/assembly/failed with orderId {}", orderId);
        return orderService.setAssemblyFailed(orderId);
    }
}