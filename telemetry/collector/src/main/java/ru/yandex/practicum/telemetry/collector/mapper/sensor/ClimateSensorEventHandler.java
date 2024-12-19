package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import static ru.yandex.practicum.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(ClimateSensorAvro.newBuilder()
                        .setTemperatureC(event.getClimateSensor().getTemperatureC())
                        .setCo2Level(event.getClimateSensor().getCo2Level())
                        .setHumidity(event.getClimateSensor().getHumidity())
                        .build())
                .build();
    }
}