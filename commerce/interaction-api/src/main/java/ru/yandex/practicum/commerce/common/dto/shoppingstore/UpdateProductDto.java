package ru.yandex.practicum.commerce.common.dto.shoppingstore;

import jakarta.validation.constraints.*;
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
public class UpdateProductDto {
    @NotNull
    UUID productId;

    @NotBlank
    @Size(min = 5, max = 255)
    String productName;

    @NotBlank
    @Size(min = 10, max = 7000)
    String description;

    String imageSrc;

    @NotNull
    QuantityState quantityState;

    @NotNull
    ProductState productState;

    @Min(1)
    @Max(5)
    Double rating;

    @NotNull
    ProductCategory productCategory;

    @Min(1)
    Double price;
}