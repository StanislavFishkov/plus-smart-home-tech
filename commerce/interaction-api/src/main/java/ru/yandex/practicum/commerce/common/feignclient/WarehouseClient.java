package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.*;
import ru.yandex.practicum.commerce.common.dto.AddressDto;

import java.util.Map;
import java.util.UUID;

@Validated
@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseFallback.class)
public interface WarehouseClient {
    @GetMapping("/address")
    AddressDto getAddress();

    @PutMapping
    void createProduct(@Valid @RequestBody NewProductDto newProductDto);

    @PostMapping("/add")
    void addProduct(@Valid @RequestBody AddProductQuantityDto addProductQuantityDto);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantities(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/shipped")
    void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequestDto shippedToDeliveryRequestDto);

    @PostMapping("/return")
    void returnProducts(@RequestBody @NotEmpty Map<@NotNull UUID, @Min(1) Integer> products);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(@Valid @RequestBody AssemblyProductsForOrderRequestDto assemblyProductsForOrderRequestDto);
}