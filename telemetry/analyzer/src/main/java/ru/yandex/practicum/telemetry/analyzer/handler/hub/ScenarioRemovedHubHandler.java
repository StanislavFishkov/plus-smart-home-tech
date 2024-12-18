package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedHubHandler implements HubHandler {
    private final ScenarioRepository scenarioRepository;

    @Override
    public String getHandledClassName() {
        return ScenarioRemovedEventAvro.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(HubEventAvro hubEventAvro) {
        String scenarioName = ((ScenarioRemovedEventAvro) hubEventAvro.getPayload()).getName();
        scenarioRepository.deleteByHubIdAndName(hubEventAvro.getHubId(), scenarioName);
        log.info("Scenario deleted from db by hub id and by name: {}, {}", hubEventAvro.getHubId(), scenarioName);
    }
}