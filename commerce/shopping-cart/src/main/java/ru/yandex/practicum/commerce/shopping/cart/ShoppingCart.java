package ru.yandex.practicum.commerce.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.common.feignclient")
@Import({ru.yandex.practicum.commerce.common.error.ErrorHandler.class,
        ru.yandex.practicum.commerce.common.feignclient.WarehouseFallback.class})
@SpringBootApplication
public class ShoppingCart {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCart.class, args);
    }
}