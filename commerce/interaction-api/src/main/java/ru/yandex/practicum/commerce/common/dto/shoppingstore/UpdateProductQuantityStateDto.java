package ru.yandex.practicum.commerce.common.dto.shoppingstore;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.model.QuantityState;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductQuantityStateDto {
    @NotNull
    UUID productId;

    @NotNull
    QuantityState quantityState;
}