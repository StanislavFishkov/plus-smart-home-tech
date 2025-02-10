package ru.yandex.practicum.commerce.common.dto.payment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    UUID paymentId;

    double totalPayment;

    double deliveryTotal;

    double feeTotal;
}