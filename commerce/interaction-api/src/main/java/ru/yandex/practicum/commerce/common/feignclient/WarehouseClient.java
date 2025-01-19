package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;

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
}
