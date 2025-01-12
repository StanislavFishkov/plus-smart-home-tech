package ru.yandex.practicum.commerce.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.commerce.common.dto.PageableDto;

public final class PagingUtil {
    public static PageRequest pageOf(PageableDto pageableDto) {
        return PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), Sort.Direction.ASC,
                pageableDto.getSort().toArray(new String[0]));
    }
}