package ru.yandex.practicum.commerce.shopping.cart.service;

import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getByUsername(String username);

    void deactivate(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Integer> products);

    ShoppingCartDto removeProducts(String username, Set<UUID> products);

    ShoppingCartDto updateProductQuantity(String username, UpdateProductQuantityDto updateProductQuantityDto);

    BookedProductsDto bookProductsInWarehouse(String username);
}