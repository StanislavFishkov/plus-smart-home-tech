package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    HubEventProto.PayloadCase getMessageType();

    HubEventAvro toAvro(HubEventProto event);
}