package ru.yandex.practicum.commerce.shopping.cart.service;

import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getByUsername(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Integer> products);
}
