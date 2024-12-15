package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface EventService {
    void collectSensorEvent(SensorEventProto event);

    void collectHubEvent(HubEventProto event);
}