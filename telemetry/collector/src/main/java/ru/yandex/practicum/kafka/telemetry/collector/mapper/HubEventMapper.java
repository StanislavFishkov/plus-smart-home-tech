package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.collector.dto.hub.*;
import ru.yandex.practicum.kafka.telemetry.collector.exception.ValidationException;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class HubEventMapper {
    public static HubEventAvro toAvro(HubEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(toPayloadAvro(event))
                .build();
    }

    private static SpecificRecordBase toPayloadAvro(HubEvent event) {
        switch (event.getType()) {
            case DEVICE_ADDED_EVENT -> {
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
                return DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAddedEvent.getId())
                        .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                        .build();
            }
            case DEVICE_REMOVED_EVENT -> {
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
                return DeviceRemovedEventAvro.newBuilder()
                        .setId(deviceRemovedEvent.getId())
                        .build();
            }
            case SCENARIO_ADDED_EVENT -> {
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
                return ScenarioAddedEventAvro.newBuilder()
                        .setName(scenarioAddedEvent.getName())
                        .setConditions(scenarioAddedEvent.getConditions().stream()
                                .map(HubEventMapper::toScenarioConditionAvro)
                                .toList()
                        )
                        .setActions(scenarioAddedEvent.getActions().stream()
                                .map(HubEventMapper::toDeviceActionAvro)
                                .toList()
                        )
                        .build();
            }
            case SCENARIO_REMOVED_EVENT -> {
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
                return ScenarioRemovedEventAvro.newBuilder()
                        .setName(scenarioRemovedEvent.getName())
                        .build();
            }
            default -> throw new ValidationException("Wrong type of hub event: " + event.getType());
        }
    }

    private static ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getConditionType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getConditionOperation().name()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private static DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getActionType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }
}