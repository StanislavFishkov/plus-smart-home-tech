package ru.yandex.practicum.kafka.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import static ru.yandex.practicum.kafka.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class TemperatureSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(event.getTemperatureSensor().getTemperatureC())
                        .setTemperatureF(event.getTemperatureSensor().getTemperatureF())
                        .build())
                .build();
    }
}