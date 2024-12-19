package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.broker.ProducerBroker;
import ru.yandex.practicum.telemetry.collector.config.CollectorTopics;
import ru.yandex.practicum.telemetry.collector.exception.ValidationException;
import ru.yandex.practicum.telemetry.collector.mapper.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.mapper.sensor.SensorEventHandler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.telemetry.collector.util.TimestampProto.toEpochMilli;

@Slf4j
@Service
@EnableConfigurationProperties(CollectorTopics.class)
public class EventServiceImpl implements EventService {
    private final ProducerBroker producerBroker;
    private final CollectorTopics collectorTopics;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventServiceImpl(ProducerBroker producerBroker,
                            CollectorTopics collectorTopics,
                            Set<SensorEventHandler> sensorEventHandlers,
                            Set<HubEventHandler> hubEventHandlers
                            ) {
        this.producerBroker = producerBroker;
        this.collectorTopics = collectorTopics;

        // Преобразовываем набор хендлеров в map, где ключ — тип события от конкретного датчика или хаба.
        // Это нужно для упрощения поиска подходящего хендлера во время обработки событий
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public void collectSensorEvent(SensorEventProto event) {
        // проверяем, есть ли обработчик для полученного события
        if (sensorEventHandlers.containsKey(event.getPayloadCase())) {
            // если обработчик найден, передаём событие ему на обработку
            SensorEventAvro eventAvro = sensorEventHandlers.get(event.getPayloadCase()).toAvro(event);
            log.info("Proto sensor request transformed to Avro: {}", eventAvro);
            producerBroker.send(collectorTopics.sensorEventsTopic(), toEpochMilli(event.getTimestamp()), event.getHubId(),
                    eventAvro);
        } else {
            throw new ValidationException("Can't find handler for sensor event: " + event.getPayloadCase());
        }
    }

    @Override
    public void collectHubEvent(HubEventProto event) {
        // проверяем, есть ли обработчик для полученного события
        if (hubEventHandlers.containsKey(event.getPayloadCase())) {
            // если обработчик найден, передаём событие ему на обработку
            HubEventAvro eventAvro = hubEventHandlers.get(event.getPayloadCase()).toAvro(event);
            log.info("Proto hub request transformed to Avro: {}", eventAvro);
            producerBroker.send(collectorTopics.hubEventsTopic(), toEpochMilli(event.getTimestamp()), event.getHubId(),
                    eventAvro);
        } else {
            throw new ValidationException("Can't find handler for hub event: " + event.getPayloadCase());
        }
    }
}