package ru.yandex.practicum.commerce.common.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.AddressDto;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewOrderDto {
    @NotNull
    @Valid
    ShoppingCartDto shoppingCart;

    @NotNull
    @Valid
    AddressDto deliveryAddress;
}