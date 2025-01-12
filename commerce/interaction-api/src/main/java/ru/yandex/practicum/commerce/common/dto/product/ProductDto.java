package ru.yandex.practicum.commerce.common.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.common.model.ProductState;
import ru.yandex.practicum.commerce.common.model.QuantityState;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
