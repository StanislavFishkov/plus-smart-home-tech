package ru.yandex.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;

import ru.yandex.practicum.commerce.warehouse.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DimensionMapper.class})
public interface ProductMapper {
     @Mapping(target = "quantity", constant = "0")
     Product toEntity(NewProductDto newProductDto);
}