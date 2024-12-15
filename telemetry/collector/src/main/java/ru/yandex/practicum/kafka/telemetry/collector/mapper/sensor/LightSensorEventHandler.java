package ru.yandex.practicum.kafka.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import static ru.yandex.practicum.kafka.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class LightSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(LightSensorAvro.newBuilder()
                        .setLinkQuality(event.getLightSensor().getLinkQuality())
                        .setLuminosity(event.getLightSensor().getLuminosity())
                        .build())
                .build();
    }
}