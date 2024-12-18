package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.hub.HubHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class HubEventServiceImpl implements HubEventService {
    private final Map<String, HubHandler> hubHandlers;

    public HubEventServiceImpl(Set<HubHandler> hubHandlers) {
        this.hubHandlers = hubHandlers.stream()
                .collect(Collectors.toMap(HubHandler::getHandledClassName, Function.identity()));
    }

    @Override
    public void handleHubEvent(HubEventAvro hubEventAvro) {
        String payloadClassName = hubEventAvro.getPayload().getClass().getName();

        if (hubHandlers.containsKey(payloadClassName)) {
            hubHandlers.get(payloadClassName).handle(hubEventAvro);
        } else {
           log.error("Analyzer: no handler for class {} of hub event: {}", payloadClassName, hubEventAvro);
        }
    }
}