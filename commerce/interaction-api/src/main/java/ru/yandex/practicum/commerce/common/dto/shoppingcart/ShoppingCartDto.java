package ru.yandex.practicum.commerce.common.dto.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    UUID shoppingCartId;

    Map<UUID, Integer> products;
}
