package ru.yandex.practicum.kafka.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.collector.broker.ProducerBroker;
import ru.yandex.practicum.kafka.telemetry.collector.config.CollectorTopics;
import ru.yandex.practicum.kafka.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.SensorEventMapper;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(CollectorTopics.class)
public class EventServiceImpl implements EventService {
    private final ProducerBroker producerBroker;
    private final CollectorTopics collectorTopics;

    @Override
    public void collectSensorEvent(SensorEvent event) {
        producerBroker.send(collectorTopics.sensorEventsTopic(), event.getTimestamp().toEpochMilli(), event.getHubId(),
                SensorEventMapper.toAvro(event));
    }

    @Override
    public void collectHubEvent(HubEvent event) {
        producerBroker.send(collectorTopics.hubEventsTopic(), event.getTimestamp().toEpochMilli(), event.getHubId(),
                HubEventMapper.toAvro(event));
    }
}