package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.common.model.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @UuidGenerator
    @Column(name = "id")
    UUID deliveryId;

    @Column(name = "order_id")
    UUID orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country_from")),
            @AttributeOverride(name = "city", column = @Column(name = "city_from")),
            @AttributeOverride(name = "street", column = @Column(name = "street_from")),
            @AttributeOverride(name = "house", column = @Column(name = "house_from")),
            @AttributeOverride(name = "flat", column = @Column(name = "flat_from"))
    })
    Address fromAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "country_to")),
            @AttributeOverride(name = "city", column = @Column(name = "city_to")),
            @AttributeOverride(name = "street", column = @Column(name = "street_to")),
            @AttributeOverride(name = "house", column = @Column(name = "house_to")),
            @AttributeOverride(name = "flat", column = @Column(name = "flat_to"))
    })
    Address toAddress;

    @Column(name = "delivery_state")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    DeliveryState state = DeliveryState.CREATED;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "delivery_cost")
    Double deliveryCost;
}