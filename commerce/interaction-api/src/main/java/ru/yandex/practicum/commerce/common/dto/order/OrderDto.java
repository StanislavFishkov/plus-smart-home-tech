package ru.yandex.practicum.commerce.common.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.model.OrderState;

import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID orderId;

    UUID shoppingCartId;

    Map<UUID, Integer> products;

    UUID paymentId;

    UUID deliveryId;

    OrderState state;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    Double totalPrice;

    Double deliveryPrice;

    Double productPrice;
}