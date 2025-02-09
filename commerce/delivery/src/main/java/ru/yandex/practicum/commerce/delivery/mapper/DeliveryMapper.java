package ru.yandex.practicum.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.delivery.model.Delivery;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AddressMapper.class)
public interface DeliveryMapper {
    DeliveryDto toDto(Delivery delivery);

    Delivery toEntity(NewDeliveryDto newDeliveryDto);
}