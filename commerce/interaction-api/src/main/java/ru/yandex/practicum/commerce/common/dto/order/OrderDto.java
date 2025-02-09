package ru.yandex.practicum.commerce.common.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    UUID orderId;

    UUID shoppingCartId;

    @NotEmpty
    Map<@NotNull UUID, @Min(1) Integer> products;

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