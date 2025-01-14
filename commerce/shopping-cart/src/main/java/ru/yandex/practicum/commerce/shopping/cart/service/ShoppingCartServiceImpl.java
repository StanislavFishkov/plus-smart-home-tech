package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartProduct;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartProductRepository;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository productRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public ShoppingCartDto getByUsername(String username) {
        return shoppingCartMapper.toDto(getOrCreateShoppingCartByUsername(username));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products) {
        ShoppingCart shoppingCart = getOrCreateShoppingCartByUsername(username);

        Map<UUID, ShoppingCartProduct> shoppingCartProducts = shoppingCart.getProducts();
        for (Map.Entry<UUID, Integer> productQuantity: products.entrySet()) {
            ShoppingCartProduct shoppingCartProduct;
            if (shoppingCartProducts.containsKey(productQuantity.getKey())) {
                shoppingCartProduct = shoppingCartProducts.get(productQuantity.getKey());
            } else {
                shoppingCartProduct = ShoppingCartProduct.builder()
                        .shoppingCart(shoppingCart)
                        .productId(productQuantity.getKey())
                        .build();
                shoppingCartProducts.put(shoppingCartProduct.getProductId(), shoppingCartProduct);
            }
            shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + productQuantity.getValue());
            productRepository.save(shoppingCartProduct);
        }

        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getOrCreateShoppingCartByUsername(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> shoppingCartRepository.save(ShoppingCart.builder().username(username).build()));
    }
}
