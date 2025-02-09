package ru.yandex.practicum.commerce.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    @NotBlank
    String country;

    @NotBlank
    String city;

    @NotBlank
    String street;

    @NotBlank
    String house;

    String flat;
}