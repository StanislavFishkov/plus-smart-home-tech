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
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PutMapping
    public OrderDto create(@NotBlank @RequestParam("username") String username,
                           @Valid @RequestBody NewOrderDto newOrderDto) {
        log.trace("PUT /api/v1/order with username {} and NewOrderDto {}", username, newOrderDto);
        return orderService.create(username, newOrderDto);
    }

    @GetMapping
    public List<OrderDto> get(@NotBlank @RequestParam("username") String username, @Valid PageableDto pageableDto) {
        log.trace("GET /api/v1/order with username {} and PageableDto {}", username, pageableDto);
        return orderService.getByUsername(username, pageableDto);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequestDto productReturnRequestDto) {
        log.trace("POST /api/v1/order/return with ProductReturnRequestDto {}", productReturnRequestDto);
        return orderService.returnOrder(productReturnRequestDto);
    }

    @PostMapping("/payment")
    public OrderDto setPaymentMade(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/payment with orderId {}", orderId);
        return orderService.setPaymentMade(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto setPaymentFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/payment/failed with orderId {}", orderId);
        return orderService.setPaymentFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto setDeliveryMade(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/delivery with orderId {}", orderId);
        return orderService.setDeliveryMade(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto setDeliveryFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/delivery/failed with orderId {}", orderId);
        return orderService.setDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto setCompleted(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/completed with orderId {}", orderId);
        return orderService.setCompleted(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/calculate/total with orderId {}", orderId);
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/calculate/delivery with orderId {}", orderId);
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/assembly with orderId {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto setAssemblyFailed(@RequestParam UUID orderId) {
        log.trace("POST /api/v1/order/assembly/failed with orderId {}", orderId);
        return orderService.setAssemblyFailed(orderId);
    }
}