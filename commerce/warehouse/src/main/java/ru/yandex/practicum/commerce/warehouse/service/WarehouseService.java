package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.AddressDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;

public interface WarehouseService {
    AddressDto getAddress();

    void createProduct(NewProductDto newProductDto);

    void addProduct(AddProductQuantityDto addProductQuantityDto);

    BookedProductsDto checkProductQuantities(ShoppingCartDto shoppingCartDto);
}