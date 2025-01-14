package ru.yandex.practicum.commerce.shopping.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.commerce.common.dto.product.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.product.ProductDto;
import ru.yandex.practicum.commerce.common.dto.product.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.product.UpdateProductQuantityStateDto;
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