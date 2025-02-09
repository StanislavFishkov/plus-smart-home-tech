package ru.yandex.practicum.commerce.common.dto.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.dto.AddressDto;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewDeliveryDto {
    @NotNull
    UUID orderId;

    @NotNull
    @Valid
    AddressDto fromAddress;

    @NotNull
    @Valid
    AddressDto toAddress;
}