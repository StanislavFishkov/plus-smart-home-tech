package ru.yandex.practicum.commerce.shopping.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.service.ShoppingCartService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto get(@RequestParam("username") String username) {
        log.trace("GET /api/v1/shopping-cart by username: {}", username);
        return shoppingCartService.getByUsername(username);
    }

    @PutMapping
    public ShoppingCartDto addProducts(@RequestParam("username") String username,
                                       @RequestBody Map<UUID, Integer> products) {
        log.trace("PUT /api/v1/shopping-cart by username: {} with body: {}", username, products);
        return shoppingCartService.addProducts(username, products);
    }
}