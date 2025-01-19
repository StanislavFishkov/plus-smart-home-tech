package ru.yandex.practicum.commerce.shopping.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID>  {
    Optional<ShoppingCart> findByUsernameAndActiveTrue(String username);

    Optional<ShoppingCart> deleteByUsernameAndActiveTrue(String username);
}
