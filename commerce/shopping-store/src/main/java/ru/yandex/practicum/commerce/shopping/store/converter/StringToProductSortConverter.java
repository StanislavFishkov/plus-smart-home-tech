package ru.yandex.practicum.commerce.shopping.store.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.shopping.store.dto.ProductSort;

@Component
public class StringToProductSortConverter implements Converter<String, ProductSort> {
    @Override
    public ProductSort convert(String source) {
        for (ProductSort productSort : ProductSort.values()) {
            if (productSort.name().replaceAll("_", "").equalsIgnoreCase(source)) {
                return productSort;
            }
        }
        throw new IllegalArgumentException("No such value for ProductSort: " + source);
    }
}
