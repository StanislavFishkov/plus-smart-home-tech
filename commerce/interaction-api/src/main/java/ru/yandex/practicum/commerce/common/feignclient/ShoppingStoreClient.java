package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductQuantityStateDto;
import ru.yandex.practicum.commerce.common.model.ProductCategory;

import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @PutMapping
    ProductDto createProduct(@Valid @RequestBody NewProductDto newProductDto);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable("productId") UUID productId);

    @GetMapping
    List<ProductDto> getProducts(@RequestParam("category") ProductCategory productCategory,
                                 @Valid PageableDto pageableDto);

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody UpdateProductDto updateProductDto);

    @PostMapping("/quantityState")
    boolean setProductQuantityState(@Valid UpdateProductQuantityStateDto updateProductQuantityStateDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);
}