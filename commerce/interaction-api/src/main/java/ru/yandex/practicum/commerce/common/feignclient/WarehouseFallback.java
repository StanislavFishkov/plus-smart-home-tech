package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.*;
import ru.yandex.practicum.commerce.common.dto.AddressDto;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class WarehouseFallback implements WarehouseClient {
    @Override
    public AddressDto getAddress() {
        log.warn("Service warehouse called for getAddress while unavailable");
        return null;
    }

    @Override
    public void createProduct(NewProductDto newProductDto) {
        log.warn("Service warehouse called for createProduct while unavailable with param {}", newProductDto);
    }

    @Override
    public void addProduct(AddProductQuantityDto addProductQuantityDto) {
        log.warn("Service warehouse called for addProduct while unavailable with param {}", addProductQuantityDto);
    }

    @Override
    public BookedProductsDto checkProductQuantities(ShoppingCartDto shoppingCartDto) {
        log.warn("Service warehouse called for checkProductQuantities while unavailable with param {}", shoppingCartDto);
        return null;
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequestDto shippedToDeliveryRequestDto) {
        log.warn("Service warehouse called for shippedToDelivery while unavailable with param {}", shippedToDeliveryRequestDto);
    }

    @Override
    public void returnProducts(Map<@NotNull UUID, @Min(0) Integer> products) {
        log.warn("Service warehouse called for returnProducts while unavailable with param {}", products);
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequestDto assemblyProductsForOrderRequestDto) {
        log.warn("Service warehouse called for assemblyProductForOrderFromShoppingCart while unavailable with param {}",
                assemblyProductsForOrderRequestDto);
        return null;
    }
}
