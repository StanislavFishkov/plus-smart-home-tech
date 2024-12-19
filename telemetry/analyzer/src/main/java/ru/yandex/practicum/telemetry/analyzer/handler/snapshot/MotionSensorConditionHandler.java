package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Slf4j
@Component
@RequiredArgsConstructor
public class MotionSensorConditionHandler implements ConditionHandler {
    private final ConditionValueComparator conditionValueComparator;

    @Override
    public String getHandledClassName() {
        return MotionSensorAvro.class.getName();
    }

    @Override
    public boolean check(Condition condition, SensorStateAvro sensorStateAvro) {
        MotionSensorAvro sensor = (MotionSensorAvro) sensorStateAvro.getData();

        Integer value = switch (condition.getType()) {
            case MOTION -> Boolean.compare(sensor.getMotion(), false);
            case SWITCH -> Boolean.compare((sensor.getLinkQuality() > 0) && (sensor.getVoltage() > 0), false);
            default -> {
                log.error("Motion sensor doesn't have value {}. Condition can't be checked.", condition.getType());
                yield null;
            }
        };

        if (value == null) return false;

        return conditionValueComparator.compare(condition.getOperation(), value, condition.getValue());
    }
}