package ru.yandex.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.common.feignclient.PaymentClient;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/payment")
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @PostMapping
    @Override
    public PaymentDto create(@Valid @RequestBody OrderDto orderDto) {
        log.trace("POST /api/v1/payment with OrderDto {}", orderDto);
        return paymentService.create(orderDto);
    }

    @PostMapping("/totalCost")
    @Override
    public Double calculateTotalCost(@Valid @RequestBody OrderDto orderDto) {
        log.trace("POST /api/v1/payment/totalCost with OrderDto {}", orderDto);
        return paymentService.calculateTotalCost(orderDto);
    }

    @PostMapping("/refund")
    @Override
    public void setPaymentSuccess(UUID paymentId) {
        log.trace("POST /api/v1/payment/refund with paymentId {}", paymentId);
        paymentService.setPaymentSuccess(paymentId);
    }

    @PostMapping("/productCost")
    @Override
    public Double calculateProductCost(@Valid @RequestBody OrderDto orderDto) {
        log.trace("POST /api/v1/payment/productCost with OrderDto {}", orderDto);
        return paymentService.calculateProductCost(orderDto);
    }

    @PostMapping("/failed")
    @Override
    public void setPaymentFailed(UUID paymentId) {
        log.trace("POST /api/v1/payment/failed with paymentId {}", paymentId);
        paymentService.setPaymentFailed(paymentId);
    }
}