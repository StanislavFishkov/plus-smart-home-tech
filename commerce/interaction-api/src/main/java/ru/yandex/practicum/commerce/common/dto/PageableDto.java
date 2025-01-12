package ru.yandex.practicum.commerce.common.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableDto {
    @PositiveOrZero
    @Builder.Default
    Integer page = 0;

    @Positive
    @Builder.Default
    Integer size = 10;

    @Builder.Default
    List<String> sort = new ArrayList<>();
}