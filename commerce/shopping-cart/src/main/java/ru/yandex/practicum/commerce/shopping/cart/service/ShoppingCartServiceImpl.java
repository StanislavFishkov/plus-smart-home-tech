package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.BookedProductsDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.commerce.common.dto.shoppingcart.UpdateProductQuantityDto;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartProduct;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartProductRepository;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartRepository;

import java.util.*;
import java.util.stream.Collectors;

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
        log.info("Active ShoppingCart is requested by username {}", username);
        return shoppingCartMapper.toDto(getOrCreateActiveShoppingCartByUsername(username));
    }

    @Override
    @Transactional
    public void deactivate(String username) {
        ShoppingCart shoppingCart = getActiveShoppingCartByUsername(username);

        shoppingCart.setActive(false);
        shoppingCartRepository.save(shoppingCart);
        log.info("ShoppingCart with id {} is deactivated", shoppingCart.getShoppingCartId());
    }

    @Override
    @Transactional
    public ShoppingCartDto addProducts(String username, Map<UUID, Integer> products) {
        ShoppingCart shoppingCart = getOrCreateActiveShoppingCartByUsername(username);

        Map<UUID, ShoppingCartProduct> shoppingCartProducts = shoppingCart.getProducts();
        for (Map.Entry<UUID, Integer> productQuantity : products.entrySet()) {
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
        log.info("Products {} are added to ShoppingCart with id {}", products, shoppingCart.getShoppingCartId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProducts(String username, Set<UUID> products) {
        ShoppingCart shoppingCart = getActiveShoppingCartByUsername(username);
        Map<UUID, ShoppingCartProduct> shoppingCartProducts = shoppingCart.getProducts();

        // check products exist in shoppingCart
        products.stream()
                .filter(productId -> !shoppingCartProducts.containsKey(productId))
                .map(productId -> String.format("ShoppingCart with id %s doesn't contain Product with id %s",
                        shoppingCart.getShoppingCartId(), productId))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .filter(l -> !l.isEmpty())
                .ifPresent(l -> {
                    throw new NotFoundException(l.toString());
                });

        // delete products from db
        productRepository.deleteAll(shoppingCartProducts.entrySet()
                .stream()
                .filter(p -> products.contains(p.getKey()))
                .map(Map.Entry::getValue)
                .toList()
        );

        shoppingCartProducts.keySet().removeAll(products);
        log.info("Products with ids {} are removed from ShoppingCart with id {}", products, shoppingCart.getShoppingCartId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateProductQuantity(String username, UpdateProductQuantityDto updateProductQuantityDto) {
        ShoppingCart shoppingCart = getActiveShoppingCartByUsername(username);

        ShoppingCartProduct product = productRepository.findByShoppingCartAndProductId(shoppingCart,
                        updateProductQuantityDto.getProductId()).orElseThrow(() -> new NotFoundException(
                                String.format("ShoppingCart with id %s doesn't contain Product with id %s",
                                        shoppingCart.getShoppingCartId(), updateProductQuantityDto.getProductId())));

        product.setQuantity(updateProductQuantityDto.getNewQuantity());
        productRepository.save(product);
        log.info("Product quantity is updated: {}", updateProductQuantityDto);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public BookedProductsDto bookProductsInWarehouse(String username) {
        return null;
    }

    private ShoppingCart getOrCreateActiveShoppingCartByUsername(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> shoppingCartRepository.save(ShoppingCart.builder().username(username).build()));
    }

    private ShoppingCart getActiveShoppingCartByUsername(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("Active ShoppingCart doesn't exist by username " + username));
    }
}
