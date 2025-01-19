package ru.yandex.practicum.commerce.common.feignclient;

import jakarta.validation.constraints.Min;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.BookedProductsDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {
    @GetMapping
    ShoppingCartDto get(@RequestParam("username") String username);

    @DeleteMapping
    void deactivate(@RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProducts(@RequestParam("username") String username,
                                @RequestBody Map<UUID, @Min(0) Integer> products);

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@RequestParam("username") String username, @RequestBody Set<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto updateProductQuantity(@RequestParam("username") String username,
                                          @RequestBody UpdateProductQuantityDto updateProductQuantityDto);

    @PostMapping("/booking")
    BookedProductsDto bookProductsInWarehouse(@RequestParam("username") String username);
}
