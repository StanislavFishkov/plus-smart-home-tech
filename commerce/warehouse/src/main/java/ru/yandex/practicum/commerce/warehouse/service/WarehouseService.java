package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.*;
import ru.yandex.practicum.commerce.common.dto.AddressDto;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    AddressDto getAddress();

    void createProduct(NewProductDto newProductDto);

    void addProduct(AddProductQuantityDto addProductQuantityDto);

    BookedProductsDto checkProductQuantities(ShoppingCartDto shoppingCartDto);

    void shippedToDelivery(ShippedToDeliveryRequestDto shippedToDeliveryRequestDto);

    void returnProducts(Map<UUID, Integer> productsToReturn);

    BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequestDto assemblyProductsForOrderRequestDto);
}