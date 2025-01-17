package ru.yandex.practicum.commerce.shopping.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.NewProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductDto;
import ru.yandex.practicum.commerce.common.dto.shoppingstore.UpdateProductQuantityStateDto;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.error.exception.ValidationException;
import ru.yandex.practicum.commerce.common.model.ProductCategory;
import ru.yandex.practicum.commerce.common.dto.PageableDto;
import ru.yandex.practicum.commerce.common.model.ProductState;
import ru.yandex.practicum.commerce.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.commerce.shopping.store.model.Product;
import ru.yandex.practicum.commerce.shopping.store.repository.ProductRepository;
import ru.yandex.practicum.commerce.common.util.PagingUtil;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto create(NewProductDto newProductDto) {
        if (newProductDto.getProductId() != null && productRepository.existsById(newProductDto.getProductId()))
            throw new ValidationException("Product already exists with id " + newProductDto.getProductId());

        Product product = productRepository.save(productMapper.toEntity(newProductDto));
        log.info("Product is created: {} with id {}", newProductDto, product.getProductId());
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto get(UUID productId) {
        log.trace("Product is requested by id {}", productId);
        return productMapper.toDto(checkAndGetUserById(productId));
    }

    @Override
    public List<ProductDto> get(ProductCategory productCategory, PageableDto pageableDto) {
        log.trace("Products are requested by category {}", productCategory);
        return productMapper.toDto(productRepository.findAllByProductCategory(productCategory,
                PagingUtil.pageOf(pageableDto)));
    }

    @Override
    @Transactional
    public ProductDto update(UpdateProductDto updateProductDto) {
        if (!productRepository.existsById(updateProductDto.getProductId()))
            throw new NotFoundException("Product doesn't exists with id " + updateProductDto.getProductId());

        Product product = productRepository.save(productMapper.toEntity(updateProductDto));
        log.info("Product is updated: {}", updateProductDto);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public boolean setQuantityState(UpdateProductQuantityStateDto updateProductQuantityStateDto) {
        Product product = checkAndGetUserById(updateProductQuantityStateDto.getProductId());

        product = productRepository.save(productMapper.update(product, updateProductQuantityStateDto));
        log.info("Product's quantity state is updated: {}", updateProductQuantityStateDto);
        return product.getQuantityState().equals(updateProductQuantityStateDto.getQuantityState());
    }

    @Override
    @Transactional
    public boolean removeFromStore(UUID productId) {
        Product product = checkAndGetUserById(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        log.info("Product is removed from the store (deactivated) with id {}", productId);
        return product.getProductState() == ProductState.DEACTIVATE;
    }

    private Product checkAndGetUserById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product doesn't exist with id " + productId));
    }
}
