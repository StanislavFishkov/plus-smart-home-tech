package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedHubHandler implements HubHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getHandledClassName() {
        return DeviceRemovedEventAvro.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(HubEventAvro hubEventAvro) {
        String sensorId = ((DeviceRemovedEventAvro) hubEventAvro.getPayload()).getId();
        sensorRepository.deleteById(sensorId);
        log.info("Sensor deleted from db by id: {}", sensorId);
    }
}
