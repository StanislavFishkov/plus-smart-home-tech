package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import static ru.yandex.practicum.kafka.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(MotionSensorAvro.newBuilder()
                        .setLinkQuality(event.getMotionSensor().getLinkQuality())
                        .setMotion(event.getMotionSensor().getMotion())
                        .setVoltage(event.getMotionSensor().getVoltage())
                        .build())
                .build();
    }
}