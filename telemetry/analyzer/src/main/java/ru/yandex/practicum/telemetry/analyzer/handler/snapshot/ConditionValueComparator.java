package ru.yandex.practicum.telemetry.analyzer.handler.snapshot;

import ru.yandex.practicum.telemetry.analyzer.model.ConditionOperation;

import java.util.Objects;

public interface ConditionValueComparator {
    default boolean compare(ConditionOperation conditionOperation, Integer value, Integer baseValue) {
        return switch (conditionOperation) {
            case EQUALS -> Objects.equals(value, baseValue);
            case GREATER_THAN -> value > baseValue;
            case LOWER_THAN -> value < baseValue;
        };
    }
}