package ru.yandex.practicum.commerce.shopping.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartProduct;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, UUID> {
    List<ShoppingCartProduct> findByShoppingCartAndProductIdIn(ShoppingCart shoppingCart, List<UUID> productIds);
}