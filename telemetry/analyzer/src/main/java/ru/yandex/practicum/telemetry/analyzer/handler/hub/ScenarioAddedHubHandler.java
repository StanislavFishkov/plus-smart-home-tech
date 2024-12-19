package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.model.*;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedHubHandler implements HubHandler {
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public String getHandledClassName() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(HubEventAvro hubEventAvro) {
        Optional<Scenario> scenarioOptional = toScenario(hubEventAvro);
        if (scenarioOptional.isPresent()) {
            Scenario scenario = scenarioOptional.get();

            conditionRepository.saveAll(scenario.getConditions());
            actionRepository.saveAll(scenario.getActions());
            scenarioRepository.save(scenario);
            log.info("Scenario saved to db: {}", scenario);
        }
    }

    private Optional<Scenario> toScenario(HubEventAvro hubEventAvro) {
        ScenarioAddedEventAvro scenarioAvro = (ScenarioAddedEventAvro) hubEventAvro.getPayload();

        // check all sensors exist
        List<String> sensorIds = Stream.concat(scenarioAvro.getConditions().stream().map(ScenarioConditionAvro::getSensorId),
                        scenarioAvro.getActions().stream().map(DeviceActionAvro::getSensorId))
                .distinct()
                .toList();

        Map<String, Sensor> sensors = sensorRepository.findAllById(sensorIds)
                .stream()
                .collect(Collectors.toMap(Sensor::getId, Function.identity()));

        if (sensors.size() < sensorIds.size()) {
            log.error("Scenario {} can't be added: not all sensors {} exist within hub {}",
                    scenarioAvro, sensorIds, hubEventAvro.getHubId());
            return Optional.empty();
        }

        // get existing scenario or add new
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(), scenarioAvro.getName())
                .orElseGet(Scenario::new);

        if (scenario.getId() == null) {
            scenario.setHubId(hubEventAvro.getHubId());
            scenario.setName(scenarioAvro.getName());
        } else {
            conditionRepository.deleteByScenario(scenario);
            actionRepository.deleteByScenario(scenario);
        }

        // add conditions
        List<Condition> conditions = scenario.getConditions();
        conditions.clear();

        for (ScenarioConditionAvro conditionAvro: scenarioAvro.getConditions()) {
            conditions.add(Condition.builder()
                    .type(ConditionType.valueOf(conditionAvro.getType().name()))
                    .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                    .value(conditionAvro.getValue() instanceof Boolean ?
                            Boolean.compare((Boolean) conditionAvro.getValue(),false) :
                            (Integer) conditionAvro.getValue())
                    .sensor(sensors.get(conditionAvro.getSensorId()))
                    .scenario(scenario)
                    .build()
            );
        }

        // add actions
        List<Action> actions = scenario.getActions();
        actions.clear();

        for (DeviceActionAvro actionAvro: scenarioAvro.getActions()) {
            actions.add(Action.builder()
                    .type(ActionType.valueOf(actionAvro.getType().name()))
                    .value(actionAvro.getValue())
                    .sensor(sensors.get(actionAvro.getSensorId()))
                    .scenario(scenario)
                    .build()
            );
        }

        return Optional.of(scenario);
    }
}
