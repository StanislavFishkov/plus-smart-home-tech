package ru.yandex.practicum.commerce.shopping.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartProduct;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {

    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    default Map<UUID, Integer> map(Map<UUID, ShoppingCartProduct> products) {
        return products.values().stream()
                .collect(Collectors.toMap(ShoppingCartProduct::getProductId, ShoppingCartProduct::getQuantity));
    }
}
