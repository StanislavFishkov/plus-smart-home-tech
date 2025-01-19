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
public class AddProductQuantityDto {
    @NotNull
    UUID productId;

    @Min(1)
    @NotNull
    Integer quantity;
}