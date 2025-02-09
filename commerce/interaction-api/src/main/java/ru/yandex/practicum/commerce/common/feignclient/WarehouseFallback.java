package ru.yandex.practicum.commerce.common.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.AddProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.AddressDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.NewProductDto;

@Slf4j
@Component
public class WarehouseFallback implements WarehouseClient {
    @Override
    public AddressDto getAddress() {
        log.warn("Service warehouse called for getAddress while unavailable");
        return AddressDto.builder()
                .country("Warehouse")
                .city("is")
                .street("not")
                .house("available")
                .flat("now")
                .build();
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
}
