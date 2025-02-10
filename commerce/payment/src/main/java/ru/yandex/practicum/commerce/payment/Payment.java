package ru.yandex.practicum.commerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.common.feignclient")
@Import({ru.yandex.practicum.commerce.common.error.ErrorHandler.class})
@SpringBootApplication
public class Payment {
    public static void main(String[] args) {
        SpringApplication.run(Payment.class, args);
    }
}