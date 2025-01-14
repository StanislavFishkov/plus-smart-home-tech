package ru.yandex.practicum.commerce.common.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.common.model.ProductState;
import ru.yandex.practicum.commerce.common.model.QuantityState;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID productId;

    String productName;

    String description;

    String imageSrc;

    QuantityState quantityState;

    ProductState productState;

    Double rating;

    ProductCategory productCategory;

    Double price;
}
