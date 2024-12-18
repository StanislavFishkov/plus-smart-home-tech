package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Slf4j
@Component
@RequiredArgsConstructor
public class LightSensorConditionHandler implements ConditionHandler {
    private final ConditionValueComparator conditionValueComparator;

    @Override
    public String getHandledClassName() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public boolean check(Condition condition, SensorStateAvro sensorStateAvro) {
        LightSensorAvro sensor = (LightSensorAvro) sensorStateAvro.getData();

        Integer value = switch (condition.getType()) {
            case LUMINOSITY -> sensor.getLuminosity();
            case SWITCH -> Boolean.compare(sensor.getLinkQuality() > 0, false);
            default -> {
                log.error("Light sensor doesn't have value {}. Condition can't be checked.", condition.getType());
                yield null;
            }
        };

        if (value == null) return false;

        return conditionValueComparator.compare(condition.getOperation(), value, condition.getValue());
    }
}