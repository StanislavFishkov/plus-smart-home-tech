package ru.yandex.practicum.telemetry.analyzer.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.snapshot.ConditionHandler;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SnapshotServiceImpl implements SnapshotService {
    private final ScenarioRepository scenarioRepository;
    private final Map<String, ConditionHandler> conditionHandlers;
    private final HubRouterControllerBlockingStub hubRouterClient;

    private final Map<String, Instant> lastHandledTimestamps = new HashMap<>();

    public SnapshotServiceImpl(ScenarioRepository scenarioRepository,
                               @GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient,
                               Set<ConditionHandler> conditionHandlers) {
        this.scenarioRepository = scenarioRepository;
        this.conditionHandlers = conditionHandlers.stream()
                .collect(Collectors.toMap(ConditionHandler::getHandledClassName, Function.identity()));
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void handleSnapshot(SensorsSnapshotAvro snapshotAvro) {
        if (lastHandledTimestamps.containsKey(snapshotAvro.getHubId())
                && lastHandledTimestamps.get(snapshotAvro.getHubId()).isAfter(snapshotAvro.getTimestamp())) {
            log.info("Snapshot timestamp is before last handled snapshot and thus will not be handled: {}", snapshotAvro);
            return;
        }

        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshotAvro.getHubId());

        scenarios.stream()
                .filter(s -> checkConditionsBySensorsState(s.getConditions(), snapshotAvro.getSensorsState()))
                //.peek(s -> s.getActions().size())
                .forEach(s -> handleActions(s.getActions(), s));

        log.info("Snapshot has been handled: {}", snapshotAvro);
        lastHandledTimestamps.put(snapshotAvro.getHubId(), snapshotAvro.getTimestamp());
    }

    private boolean checkConditionsBySensorsState(List<Condition> conditions, Map<String, SensorStateAvro> sensorsState) {
        List<Condition> conditionsWithErrors = conditions.stream()
                .filter(c -> !sensorsState.containsKey(c.getSensor().getId()))
                .toList();
        if (!conditionsWithErrors.isEmpty()) {
            log.info("No sensor state to check conditions: {}", conditionsWithErrors);
            return false;
        }

        conditionsWithErrors = conditions.stream()
                .filter(c -> !conditionHandlers.containsKey(sensorsState.get(c.getSensor().getId()).getData().getClass().getName()))
                .toList();
        if (!conditionsWithErrors.isEmpty()) {
            log.info("No registered handlers to check conditions: {}", conditionsWithErrors);
            return false;
        }

        return conditions.stream()
                .allMatch(c -> conditionHandlers.get(sensorsState.get(c.getSensor().getId()).getData().getClass().getName())
                        .check(c, sensorsState.get(c.getSensor().getId())));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void handleActions(List<Action> actions, final Scenario s) {
        final Instant now = Instant.now();

        actions.stream()
                .map(action ->
                        DeviceActionProto.newBuilder()
                                .setSensorId(action.getSensor().getId())
                                .setType(ActionTypeProto.valueOf(action.getType().name()))
                                .setValue(action.getValue())
                        .build())
                .forEach(deviceAction -> {
                            DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                                    .setHubId(s.getHubId())
                                    .setScenarioName(s.getName())
                                    .setAction(deviceAction)
                                    .setTimestamp(Timestamp.newBuilder()
                                            .setSeconds(now.getEpochSecond())
                                            .setNanos(now.getNano()))
                                    .build();
                            try {
                                hubRouterClient.handleDeviceAction(deviceActionRequest);
                                log.info("Device action request sent: {}", deviceActionRequest);
                            } catch (Exception e) {
                                log.error("Device action request hasn't been sent: {}\n", deviceActionRequest, e);
                            }
                        }
                );
    }
}