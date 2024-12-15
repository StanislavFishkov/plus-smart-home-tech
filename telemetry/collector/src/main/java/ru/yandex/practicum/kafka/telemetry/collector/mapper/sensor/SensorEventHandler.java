package ru.yandex.practicum.kafka.telemetry.collector.mapper.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

// Интерфейс, объединяющий все хендлеры для SensorEventProto-событий.
// Благодаря ему мы сможем внедрить все хендлеры в виде списка
// в компонент, который будет распределять получаемые события по
// их обработчикам
public interface SensorEventHandler {
    SensorEventProto.PayloadCase getMessageType();

    SensorEventAvro toAvro(SensorEventProto event);
}