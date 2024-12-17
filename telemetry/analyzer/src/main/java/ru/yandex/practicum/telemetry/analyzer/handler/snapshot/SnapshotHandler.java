package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotHandler {
    String getHandledClassName();
    void handle(SensorsSnapshotAvro snapshotAvro);
}