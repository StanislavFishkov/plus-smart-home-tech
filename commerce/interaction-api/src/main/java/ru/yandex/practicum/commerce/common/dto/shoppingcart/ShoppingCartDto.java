package ru.yandex.practicum.commerce.common.dto.shoppingcart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartDto {
    UUID shoppingCartId;

    Map<UUID, Integer> products;
}
