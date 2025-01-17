package ru.yandex.practicum.commerce.common.dto.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    String country;

    String city;

    String street;

    String house;

    String flat;
}