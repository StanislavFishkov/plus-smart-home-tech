package ru.yandex.practicum.commerce.common.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductDto {
    @NotNull
    UUID productId;

    @NotNull
    Boolean fragile;

    @NotNull
    DimensionDto dimension;

    @NotNull
    @Min(1)
    Double weight;
}