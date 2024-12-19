package ru.yandex.practicum.telemetry.analyzer.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedHubHandler implements HubHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getHandledClassName() {
        return DeviceAddedEventAvro.class.getName();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(HubEventAvro hubEventAvro) {
        Sensor sensor = toSensor(hubEventAvro);
        sensorRepository.save(sensor);
        log.info("Sensor saved to db: {}", sensor);
    }

    private Sensor toSensor(HubEventAvro hubEventAvro) {
        return Sensor.builder()
                .id(((DeviceAddedEventAvro) hubEventAvro.getPayload()).getId())
                .hubId(hubEventAvro.getHubId())
                .build();
    }
}
