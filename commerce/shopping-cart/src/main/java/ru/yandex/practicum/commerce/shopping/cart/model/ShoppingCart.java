package ru.yandex.practicum.commerce.shopping.cart.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {
    @Id
    @UuidGenerator
    @Column(name = "id")
    UUID shoppingCartId;

    @Column(name = "username")
    String username;

    @Column(name = "active")
    @Builder.Default
    Boolean active = true;

    @OneToMany(mappedBy = "shoppingCart", orphanRemoval = true)
    @MapKey(name = "productId")
    @Builder.Default
    Map<UUID, ShoppingCartProduct> products = new HashMap<>();
}