package ru.yandex.practicum.commerce.common.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductsForOrderRequestDto {
    @NotNull
    UUID orderId;

    @NotEmpty
    Map<@NotNull UUID, @Min(1) Integer> products;
}
