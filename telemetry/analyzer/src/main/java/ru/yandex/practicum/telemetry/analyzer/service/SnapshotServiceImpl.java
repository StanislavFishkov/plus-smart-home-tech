package ru.yandex.practicum.telemetry.analyzer.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Service
public class SnapshotServiceImpl implements SnapshotService {
    @Override
    public void handleSnapshot(SensorsSnapshotAvro snapshotAvro) {

    }
}
