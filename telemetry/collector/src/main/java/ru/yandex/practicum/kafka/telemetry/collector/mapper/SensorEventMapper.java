package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.collector.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.collector.error.exception.ValidationException;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class SensorEventMapper {
    public static SensorEventAvro toAvro(SensorEvent event) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(toPayloadAvro(event))
                .build();
    }

    private static SpecificRecordBase toPayloadAvro(SensorEvent event) {
        switch (event.getType()) {
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
                return MotionSensorAvro.newBuilder()
                        .setLinkQuality(motionSensorEvent.getLinkQuality())
                        .setMotion(motionSensorEvent.getMotion())
                        .setVoltage(motionSensorEvent.getVoltage())
                        .build();
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
                return TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) event;
                return LightSensorAvro.newBuilder()
                        .setLinkQuality(lightSensorEvent.getLinkQuality())
                        .setLuminosity(lightSensorEvent.getLuminosity())
                        .build();
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
                return ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                        .setCo2Level(climateSensorEvent.getCo2Level())
                        .setHumidity(climateSensorEvent.getHumidity())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
                return SwitchSensorAvro.newBuilder()
                        .setState(switchSensorEvent.getState())
                        .build();
            }
            default -> throw new ValidationException("Wrong type of sensor event: " + event.getType());
        }
    }
}