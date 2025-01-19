package ru.yandex.practicum.commerce.common.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @NotNull
    @Min(1)
    Double width;

    @NotNull
    @Min(1)
    Double height;

    @NotNull
    @Min(1)
    Double depth;
}