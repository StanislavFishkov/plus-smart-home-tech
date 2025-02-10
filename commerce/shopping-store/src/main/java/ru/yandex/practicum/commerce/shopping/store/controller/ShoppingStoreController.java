package ru.yandex.practicum.commerce.shopping.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductQuantityStateDto;
import ru.yandex.practicum.commerce.common.feignclient.ShoppingStoreClient;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.service.ProductService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ProductService productService;

    @PutMapping
    @Override
    public ProductDto createProduct(@Valid @RequestBody NewProductDto newProductDto) {
        log.trace("PUT /api/v1/shopping-store with body {}", newProductDto);
        return productService.create(newProductDto);
    }

    @GetMapping("/{productId}")
    @Override
    public ProductDto getProduct(@PathVariable("productId") UUID productId) {
        log.trace("GET /api/v1/shopping-store/{productId} with productId {}", productId);
        return productService.get(productId);
    }

    @GetMapping
    @Override
    public List<ProductDto> getProducts(@RequestParam("category") ProductCategory productCategory,
                                        @Valid PageableDto pageableDto) {
        log.trace("GET /api/v1/shopping-store with category {} and PageableDto {}", productCategory, pageableDto);
        return productService.get(productCategory, pageableDto);
    }

    @PostMapping
    @Override
    public ProductDto updateProduct(@Valid @RequestBody UpdateProductDto updateProductDto) {
        log.trace("POST /api/v1/shopping-store with body {}", updateProductDto);
        return productService.update(updateProductDto);
    }

    @PostMapping("/quantityState")
    @Override
    public boolean setProductQuantityState(@Valid UpdateProductQuantityStateDto updateProductQuantityStateDto) {
        log.trace("POST /api/v1/shopping-store/quantityState with body/parameters {}", updateProductQuantityStateDto);
        return productService.setQuantityState(updateProductQuantityStateDto);
    }

    @PostMapping("/removeProductFromStore")
    @Override
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        log.trace("POST /api/v1/shopping-store/removeProductFromStore with body {}", productId);
        return productService.removeFromStore(productId);
    }
}
