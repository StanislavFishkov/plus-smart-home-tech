package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClimateSensorConditionHandler implements ConditionHandler {
    private final ConditionValueComparator conditionValueComparator;

    @Override
    public String getHandledClassName() {
        return ClimateSensorAvro.class.getName();
    }

    @Override
    public boolean check(Condition condition, SensorStateAvro sensorStateAvro) {
        ClimateSensorAvro sensor = (ClimateSensorAvro) sensorStateAvro.getData();

        Integer value = switch (condition.getType()) {
            case TEMPERATURE -> sensor.getTemperatureC();
            case HUMIDITY -> sensor.getHumidity();
            case CO2LEVEL -> sensor.getCo2Level();
            default -> {
                log.error("Climate sensor doesn't have value {}. Condition can't be checked.", condition.getType());
                yield null;
            }
        };

        if (value == null) return false;

        return conditionValueComparator.compare(condition.getOperation(), value, condition.getValue());
    }
}