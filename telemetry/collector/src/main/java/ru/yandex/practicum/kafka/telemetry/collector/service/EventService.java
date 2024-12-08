package ru.yandex.practicum.kafka.telemetry.collector.service;

import ru.yandex.practicum.kafka.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.dto.sensor.SensorEvent;

public interface EventService {
    void collectSensorEvent(SensorEvent event);

    void collectHubEvent(HubEvent event);
}