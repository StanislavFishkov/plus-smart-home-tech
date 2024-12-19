package ru.yandex.practicum.telemetry.collector.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import static ru.yandex.practicum.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class SwitchSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(SwitchSensorAvro.newBuilder()
                        .setState(event.getSwitchSensor().getState())
                        .build())
                .build();
    }
}