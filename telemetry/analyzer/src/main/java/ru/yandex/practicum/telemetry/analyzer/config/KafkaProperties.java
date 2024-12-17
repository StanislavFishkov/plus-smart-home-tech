package ru.yandex.practicum.telemetry.analyzer.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.yandex.practicum.telemetry.avro.HubEventDeserializer;
import ru.yandex.practicum.telemetry.avro.SensorsSnapshotDeserializer;

@ConfigurationProperties(prefix = "analyzer.kafka")
public record KafkaProperties(String bootstrapServers, String hubsConsumerGroupId, String snapshotsConsumerGroupId) {
    public static final Class<?> KEY_DESERIALIZER_CLASS = StringDeserializer.class;
    public static final Class<?> HUB_EVENT_DESERIALIZER_CLASS = HubEventDeserializer.class;
    public static final Class<?> SNAPSHOT_DESERIALIZER_CLASS = SensorsSnapshotDeserializer.class;
}