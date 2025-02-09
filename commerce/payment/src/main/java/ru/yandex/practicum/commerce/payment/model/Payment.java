package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.common.model.PaymentState;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @UuidGenerator
    @Column(name = "id")
    UUID paymentId;

    @Column(name = "order_id")
    UUID orderId;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    PaymentState state = PaymentState.PENDING;

    @Column(name = "total_payment")
    double totalPayment;

    @Column(name = "delivery_total")
    double deliveryTotal;

    @Column(name = "product_total")
    double productTotal;
}