package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubHandler {
    String getHandledClassName();

    void handle(HubEventAvro hubEventAvro);
}
