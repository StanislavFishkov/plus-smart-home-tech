package ru.yandex.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.warehouse.DimensionDto;
import ru.yandex.practicum.commerce.warehouse.model.Dimension;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DimensionMapper {
     Dimension toEntity(DimensionDto dimensionDto);
}