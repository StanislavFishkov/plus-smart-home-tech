package ru.yandex.practicum.commerce.common.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.common.model.QuantityState;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductQuantityStateDto {
    @NotNull
    UUID productId;

    @NotNull
    QuantityState quantityState;
}
