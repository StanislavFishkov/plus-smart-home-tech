package ru.yandex.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.order.NewOrderDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.model.Order;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "shoppingCartId", source = "newOrderDto.shoppingCart.shoppingCartId")
    @Mapping(target = "products", source = "newOrderDto.shoppingCart.products")
    Order toEntity(String username, NewOrderDto newOrderDto);
}