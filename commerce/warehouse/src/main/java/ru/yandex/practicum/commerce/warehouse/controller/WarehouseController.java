package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.*;
import ru.yandex.practicum.commerce.common.dto.AddressDto;
import ru.yandex.practicum.commerce.common.feignclient.WarehouseClient;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/warehouse")
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @GetMapping("/address")
    @Override
    public AddressDto getAddress() {
        log.trace("POST /api/v1/warehouse/address");
        return warehouseService.getAddress();
    }

    @PutMapping
    @Override
    public void createProduct(@Valid @RequestBody NewProductDto newProductDto) {
        log.trace("PUT /api/v1/warehouse with body {}", newProductDto);
        warehouseService.createProduct(newProductDto);
    }

    @PostMapping("/add")
    @Override
    public void addProduct(@Valid @RequestBody AddProductQuantityDto addProductQuantityDto) {
        log.trace("POST /api/v1/warehouse/add with body {}", addProductQuantityDto);
        warehouseService.addProduct(addProductQuantityDto);
    }

    @PostMapping("/check")
    @Override
    public BookedProductsDto checkProductQuantities(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.trace("POST /api/v1/warehouse/check with body {}", shoppingCartDto);
        return warehouseService.checkProductQuantities(shoppingCartDto);
    }

    @PostMapping("/shipped")
    @Override
    public void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequestDto shippedToDeliveryRequestDto) {
        log.trace("POST /api/v1/warehouse/shipped with body {}", shippedToDeliveryRequestDto);
        warehouseService.shippedToDelivery(shippedToDeliveryRequestDto);
    }

    @PostMapping("/return")
    @Override
    public void returnProducts(@RequestBody @NotEmpty Map<@NotNull UUID, @Min(1) Integer> productsToReturn) {
        log.trace("POST /api/v1/warehouse/return with body {}", productsToReturn);
        warehouseService.returnProducts(productsToReturn);
    }

    @PostMapping("/assembly")
    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(@Valid @RequestBody AssemblyProductsForOrderRequestDto assemblyProductsForOrderRequestDto) {
        log.trace("POST /api/v1/warehouse/assembly with body {}", assemblyProductsForOrderRequestDto);
        return warehouseService.assemblyProductForOrderFromShoppingCart(assemblyProductsForOrderRequestDto);
    }
}