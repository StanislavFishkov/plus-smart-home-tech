package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;

import java.util.UUID;

@Validated
@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {
    @PostMapping
    PaymentDto create(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    double calculateTotalCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void setPaymentSuccess(UUID paymentId);

    @PostMapping("/productCost")
    double calculateProductCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void setPaymentFailed(UUID paymentId);
}