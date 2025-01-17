package ru.yandex.practicum.commerce.shopping.cart.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;
import ru.yandex.practicum.commerce.shopping.cart.service.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto get(@RequestParam("username") String username) {
        log.trace("GET /api/v1/shopping-cart by username {}", username);
        return shoppingCartService.getByUsername(username);
    }

    @DeleteMapping
    public void deactivate(@RequestParam("username") String username) {
        log.trace("DELETE /api/v1/shopping-cart by username {}", username);
        shoppingCartService.deactivate(username);
    }

    @PutMapping
    public ShoppingCartDto addProducts(@RequestParam("username") String username,
                                       @RequestBody Map<UUID, @Min(0) Integer> products) {
        log.trace("PUT /api/v1/shopping-cart by username {} with body {}", username, products);
        return shoppingCartService.addProducts(username, products);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProducts(@RequestParam("username") String username, @RequestBody Set<UUID> products) {
        log.trace("POST /api/v1/shopping-cart/remove by username {} with body {}", username, products);
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto updateProductQuantity(@RequestParam("username") String username,
                                                  @RequestBody UpdateProductQuantityDto updateProductQuantityDto) {
        log.trace("POST /api/v1/shopping-cart/change-quantity by username {} with body {}", username, updateProductQuantityDto);
        return shoppingCartService.updateProductQuantity(username, updateProductQuantityDto);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookProductsInWarehouse(@RequestParam("username") String username) {
        log.trace("POST /api/v1/shopping-cart/booking by username {}", username);
        return shoppingCartService.bookProductsInWarehouse(username);
    }
}