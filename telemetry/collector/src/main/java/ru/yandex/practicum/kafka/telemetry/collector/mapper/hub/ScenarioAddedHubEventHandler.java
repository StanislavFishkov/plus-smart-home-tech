package ru.yandex.practicum.kafka.telemetry.collector.mapper.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import static ru.yandex.practicum.kafka.telemetry.collector.util.TimestampProto.toInstant;

@Component
public class ScenarioAddedHubEventHandler implements HubEventHandler {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(toInstant(event.getTimestamp()))
                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(event.getScenarioAdded().getName())
                        .setConditions(event.getScenarioAdded().getConditionList().stream()
                                .map((ScenarioConditionProto sc) -> ScenarioConditionAvro.newBuilder()
                                        .setSensorId(sc.getSensorId())
                                        .setType(ConditionTypeAvro.valueOf(sc.getType().name()))
                                        .setOperation(ConditionOperationAvro.valueOf(sc.getOperation().name()))
                                        .setValue(sc.hasIntValue() ? sc.getIntValue() : sc.getBoolValue())
                                        .build())
                                .toList())
                        .setActions(event.getScenarioAdded().getActionList().stream()
                                .map((DeviceActionProto da) -> DeviceActionAvro.newBuilder()
                                        .setSensorId(da.getSensorId())
                                        .setType(ActionTypeAvro.valueOf(da.getType().name()))
                                        .setValue(da.getValue())
                                        .build())
                                .toList())
                        .build())
                .build();
    }
}