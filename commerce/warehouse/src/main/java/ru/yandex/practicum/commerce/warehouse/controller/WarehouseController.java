package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping("/address")
    public AddressDto getAddress() {
        log.trace("POST /api/v1/warehouse/address");
        return warehouseService.getAddress();
    }

    @PutMapping
    public void createProduct(@Valid @RequestBody NewProductDto newProductDto) {
        log.trace("PUT /api/v1/warehouse with body {}", newProductDto);
        warehouseService.createProduct(newProductDto);
    }

    @PostMapping("/add")
    public void addProduct(@Valid @RequestBody AddProductQuantityDto addProductQuantityDto) {
        log.trace("POST /api/v1/warehouse/add with body {}", addProductQuantityDto);
        warehouseService.addProduct(addProductQuantityDto);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantities(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.trace("POST /api/v1/warehouse/check with body {}", shoppingCartDto);
        return warehouseService.checkProductQuantities(shoppingCartDto);
    }
}