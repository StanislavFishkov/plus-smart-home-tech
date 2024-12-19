package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public interface ConditionHandler {
    String getHandledClassName();

    boolean check(Condition condition, SensorStateAvro sensorStateAvro);
}