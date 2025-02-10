package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Validated
@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto get(@NotBlank @RequestParam("username") String username);

    @DeleteMapping
    void deactivate(@NotBlank @RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProducts(@NotBlank @RequestParam("username") String username,
                                @RequestBody @NotEmpty Map<@NotNull UUID, @Min(1) Integer> products);

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@NotBlank @RequestParam("username") String username,
                                   @RequestBody @NotEmpty Set<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto updateProductQuantity(@NotBlank @RequestParam("username") String username,
                                          @Valid @RequestBody UpdateProductQuantityDto updateProductQuantityDto);

    @PostMapping("/booking")
    BookedProductsDto bookProductsInWarehouse(@NotBlank @RequestParam("username") String username);
}