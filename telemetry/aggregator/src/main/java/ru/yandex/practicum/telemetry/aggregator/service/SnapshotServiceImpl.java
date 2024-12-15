package ru.yandex.practicum.telemetry.aggregator.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SnapshotServiceImpl implements SnapshotService {
    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
//        Проверяем, есть ли снапшот для event.getHubId()
//        Если снапшот есть, то достаём его
//        Если нет, то созадём новый

        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(), SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(new HashMap<>())
                .build());

//        Проверяем, есть ли в снапшоте данные для event.getId()
//        Если данные есть, то достаём их в переменную oldState
//        Проверка, если oldState.getTimestamp() произошёл позже, чем
//        event.getTimestamp(), а также если oldState.getData() равен
//        event.getPayload(), то ничего обновлять не нужно, выходим из метода
//        вернув Optional.empty()

        if (snapshot.getSensorsState().containsKey(event.getId())) {
            SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());

            if (oldState.getTimestamp().isAfter(event.getTimestamp())
                    || oldState.getData().equals(event.getPayload()))
                return Optional.empty();
        }

        // если дошли до сюда, значит, пришли новые данные и
        // снапшот нужно обновить
//        Создаём экземпляр SensorStateAvro на основе данных события
//        Добавляем полученный экземпляр в снапшот
//        Обновляем таймстемп снапшота таймстемпом из события
//        Возвращаем снапшот - Optional.of(snapshot)

        snapshot.getSensorsState().put(event.getId(), new SensorStateAvro(event.getTimestamp(), event.getPayload()));
        snapshot.setTimestamp(event.getTimestamp());

        snapshots.put(event.getHubId(), snapshot);

        return Optional.of(snapshot);
    }
}
