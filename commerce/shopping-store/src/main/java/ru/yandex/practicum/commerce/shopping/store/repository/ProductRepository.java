package ru.yandex.practicum.commerce.shopping.store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByProductCategory(ProductCategory productCategory, Pageable page);
}