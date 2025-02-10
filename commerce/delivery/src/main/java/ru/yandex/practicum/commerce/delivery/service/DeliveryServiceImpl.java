package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.common.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.common.dto.delivery.NewDeliveryDto;
import ru.yandex.practicum.commerce.common.dto.order.OrderDto;
import ru.yandex.practicum.commerce.common.dto.warehouse.ShippedToDeliveryRequestDto;
import ru.yandex.practicum.commerce.common.error.exception.ConflictDataException;
import ru.yandex.practicum.commerce.common.error.exception.NotFoundException;
import ru.yandex.practicum.commerce.common.feignclient.OrderClient;
import ru.yandex.practicum.commerce.common.feignclient.WarehouseClient;
import ru.yandex.practicum.commerce.common.model.DeliveryState;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryCostCalculator deliveryCostCalculator;

    private final PlatformTransactionManager platformTransactionManager;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public DeliveryDto create(NewDeliveryDto newDeliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(newDeliveryDto);

        delivery = deliveryRepository.save(delivery);
        log.info("Delivery is created: {}", delivery);
        return deliveryMapper.toDto(delivery);
    }

    @Override
    public void setDeliverySuccess(UUID deliveryId) {
        UUID orderId = changeDeliveryState(deliveryId, DeliveryState.DELIVERED);

        orderClient.setDeliveryMade(orderId);
    }

    @Override
    public void setDeliveryPicked(UUID deliveryId) {
        UUID orderId = changeDeliveryState(deliveryId, DeliveryState.IN_PROGRESS);

        warehouseClient.shippedToDelivery(ShippedToDeliveryRequestDto.builder()
                .orderId(orderId)
                .deliveryId(deliveryId)
                .build());
    }

    @Override
    public void setDeliveryFailed(UUID deliveryId) {
        UUID orderId = changeDeliveryState(deliveryId, DeliveryState.FAILED);

        orderClient.setDeliveryFailed(orderId);
    }

    @Override
    @Transactional
    public double calculateDeliveryCost(OrderDto orderDto) {
        if (orderDto.getDeliveryWeight() == null || orderDto.getDeliveryVolume() == null || orderDto.getFragile() == null)
            throw new ConflictDataException(("Order weight, volume and fragile must be set before delivery cost " +
                    "calculation: %s").formatted(orderDto));

        Delivery delivery = checkAndGetDeliveryById(orderDto.getDeliveryId());

        delivery.setDeliveryWeight(orderDto.getDeliveryWeight());
        delivery.setDeliveryVolume(orderDto.getDeliveryVolume());
        delivery.setFragile(orderDto.getFragile());
        delivery.setDeliveryCost(deliveryCostCalculator.calculateDeliveryCost(delivery));

        deliveryRepository.save(delivery);
        log.info("Delivery cost has been calculated and saved: {}", delivery.getDeliveryId());
        return delivery.getDeliveryCost();
    }

    private Delivery checkAndGetDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery doesn't exist with id: %s".formatted(deliveryId)));
    }

    private UUID changeDeliveryState(UUID deliveryId, DeliveryState newDeliveryState) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        UUID orderId = transactionTemplate.execute(status -> {
            Delivery delivery = checkAndGetDeliveryById(deliveryId);
            delivery.setState(newDeliveryState);
            deliveryRepository.save(delivery);
            return delivery.getOrderId();
        });

        log.info("Delivery state has been changed to {}: {}", newDeliveryState, deliveryId);
        return orderId;
    }
}