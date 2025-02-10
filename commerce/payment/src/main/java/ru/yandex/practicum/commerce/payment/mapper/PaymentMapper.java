package ru.yandex.practicum.commerce.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.Payment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    @Mapping(target = "feeTotal", expression = "java(payment.getTotalPayment() - payment.getDeliveryTotal() - payment.getProductTotal())")
    PaymentDto toDto(Payment payment);

    List<PaymentDto> toDto(List<Payment> payments);
}