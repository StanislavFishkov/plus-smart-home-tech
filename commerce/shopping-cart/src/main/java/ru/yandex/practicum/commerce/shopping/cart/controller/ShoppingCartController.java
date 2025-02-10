package ru.yandex.practicum.commerce.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;
import ru.yandex.practicum.commerce.common.feignclient.ShoppingCartClient;
import ru.yandex.practicum.commerce.shopping.cart.service.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Override
    public ShoppingCartDto get(@NotBlank @RequestParam("username") String username) {
        log.trace("GET /api/v1/shopping-cart by username {}", username);
        return shoppingCartService.getByUsername(username);
    }

    @DeleteMapping
    @Override
    public void deactivate(@NotBlank @RequestParam("username") String username) {
        log.trace("DELETE /api/v1/shopping-cart by username {}", username);
        shoppingCartService.deactivate(username);
    }

    @PutMapping
    @Override
    public ShoppingCartDto addProducts(@NotBlank @RequestParam("username") String username,
                                       @RequestBody @NotEmpty Map<@NotNull UUID, @Min(1) Integer> products) {
        log.trace("PUT /api/v1/shopping-cart by username {} with body {}", username, products);
        return shoppingCartService.addProducts(username, products);
    }

    @PostMapping("/remove")
    @Override
    public ShoppingCartDto removeProducts(@NotBlank @RequestParam("username") String username,
                                          @RequestBody @NotEmpty Set<@NotNull UUID> products) {
        log.trace("POST /api/v1/shopping-cart/remove by username {} with body {}", username, products);
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    @Override
    public ShoppingCartDto updateProductQuantity(@NotBlank @RequestParam("username") String username,
                                                 @Valid @RequestBody UpdateProductQuantityDto updateProductQuantityDto) {
        log.trace("POST /api/v1/shopping-cart/change-quantity by username {} with body {}", username, updateProductQuantityDto);
        return shoppingCartService.updateProductQuantity(username, updateProductQuantityDto);
    }

    @PostMapping("/booking")
    @Override
    public BookedProductsDto bookProductsInWarehouse(@NotBlank @RequestParam("username") String username) {
        log.trace("POST /api/v1/shopping-cart/booking by username {}", username);
        return shoppingCartService.bookProductsInWarehouse(username);
    }
}