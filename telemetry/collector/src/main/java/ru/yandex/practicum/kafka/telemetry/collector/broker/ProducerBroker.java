package ru.yandex.practicum.kafka.telemetry.collector.broker;

import org.apache.avro.specific.SpecificRecordBase;

public interface ProducerBroker {
    void send(String topic, Long timestamp, String key, SpecificRecordBase specificRecordBase);
}