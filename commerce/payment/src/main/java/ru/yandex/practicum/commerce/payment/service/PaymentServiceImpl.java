package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.error.exception.ValidationException;
import ru.yandex.practicum.commerce.common.feignclient.OrderClient;
import ru.yandex.practicum.commerce.common.feignclient.ShoppingStoreClient;
import ru.yandex.practicum.commerce.common.model.PaymentState;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.Payment;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    private final PlatformTransactionManager platformTransactionManager;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public PaymentDto create(OrderDto orderDto) {
        if (orderDto.getTotalPrice() == null || orderDto.getProductPrice() == null || orderDto.getDeliveryPrice() == null)
            throw new ValidationException("All order prices must be calculated before payment creation: %s".formatted(orderDto));

        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .productTotal(orderDto.getProductPrice())
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment is created: {}", payment);
        return paymentMapper.toDto(payment);
    }

    @Override
    public Double calculateTotalCost(OrderDto orderDto) {
        if (orderDto.getProductPrice() == null || orderDto.getDeliveryPrice() == null)
            throw new ValidationException("Product and delivery prices must be calculated before calculating total: %s".formatted(orderDto));

        return orderDto.getProductPrice() + calculateFeeCost(orderDto.getProductPrice()) + orderDto.getDeliveryPrice();
    }

    @Override
    public void setPaymentSuccess(UUID paymentId) {
        UUID orderId = changePaymentState(paymentId, PaymentState.SUCCESS);

        orderClient.setPaymentMade(orderId);
    }

    @Override
    public Double calculateProductCost(OrderDto orderDto) {
        return orderDto.getProducts().entrySet().stream()
                .map(e -> shoppingStoreClient.getProduct(e.getKey()).getPrice() * e.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    @Override
    public void setPaymentFailed(UUID paymentId) {
        UUID orderId = changePaymentState(paymentId, PaymentState.FAILED);

        orderClient.setPaymentFailed(orderId);
    }

    private Payment checkAndGetPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment doesn't exist with id: %s".formatted(paymentId)));
    }

    private UUID changePaymentState(UUID paymentId, PaymentState newPaymentState) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        UUID orderId = transactionTemplate.execute(status -> {
            Payment payment = checkAndGetPaymentById(paymentId);
            payment.setState(newPaymentState);
            paymentRepository.save(payment);
            return payment.getOrderId();
        });

        log.info("Payment state has been changed to {}: {}", newPaymentState, paymentId);
        return orderId;
    }

    private double calculateFeeCost(double productCost) {
        return productCost * 0.1;
    }
}