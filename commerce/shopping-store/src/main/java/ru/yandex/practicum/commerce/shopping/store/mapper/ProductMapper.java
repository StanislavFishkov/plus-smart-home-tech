package ru.yandex.practicum.commerce.shopping.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductQuantityStateDto;
import ru.yandex.practicum.commerce.shopping.store.model.Product;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);

    Product toEntity(NewProductDto newProductDto);

    Product toEntity(UpdateProductDto updateProductDto);

    Product update(@MappingTarget Product product, UpdateProductQuantityStateDto updateProductQuantityStateDto);
}