package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "id")
    UUID productId;

    @Column(name = "fragile")
    Boolean fragile;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "width", column = @Column(name = "width")),
            @AttributeOverride(name = "height", column = @Column(name = "height")),
            @AttributeOverride(name = "depth", column = @Column(name = "depth"))
    })
    Dimension dimension;

    @Column(name = "weight")
    Double weight;

    @Column(name = "quantity")
    @Builder.Default
    Integer quantity = 0;
}