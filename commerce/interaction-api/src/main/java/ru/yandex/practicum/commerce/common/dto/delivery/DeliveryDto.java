package ru.yandex.practicum.commerce.common.dto.delivery;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.dto.AddressDto;
import ru.yandex.practicum.commerce.common.model.DeliveryState;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    UUID deliveryId;

    UUID orderId;

    AddressDto fromAddress;

    AddressDto toAddress;

    DeliveryState deliveryState;
}