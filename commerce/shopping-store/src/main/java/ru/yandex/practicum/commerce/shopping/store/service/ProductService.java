package ru.yandex.practicum.commerce.shopping.store.service;

import ru.yandex.practicum.commerce.common.dto.shoppingstore.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductQuantityStateDto;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.common.dto.PageableDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto create(NewProductDto newProductDto);

    ProductDto get(UUID productId);

    List<ProductDto> get(ProductCategory productCategory, PageableDto pageableDto);

    ProductDto update(UpdateProductDto updateProductDto);

    boolean setQuantityState(UpdateProductQuantityStateDto updateProductQuantityStateDto);

    boolean removeFromStore(UUID productId);
}