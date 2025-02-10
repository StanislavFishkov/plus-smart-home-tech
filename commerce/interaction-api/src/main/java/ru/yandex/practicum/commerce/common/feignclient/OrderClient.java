package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.order.ProductReturnRequestDto;

import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
    @PutMapping
    OrderDto create(@NotBlank @RequestParam("username") String username,
                    @Valid @RequestBody NewOrderDto newOrderDto);

    @GetMapping
    List<OrderDto> get(@NotBlank @RequestParam("username") String username, @Valid PageableDto pageableDto);

    @PostMapping("/return")
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequestDto productReturnRequestDto);

    @PostMapping("/payment")
    OrderDto setPaymentMade(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto setPaymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto setDeliveryMade(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto setDeliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto setCompleted(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto setAssemblyFailed(@RequestBody UUID orderId);
}