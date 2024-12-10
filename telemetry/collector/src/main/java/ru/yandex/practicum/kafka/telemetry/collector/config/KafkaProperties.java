package ru.yandex.practicum.kafka.telemetry.collector.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.yandex.practicum.kafka.telemetry.collector.serializer.CollectorAvroSerializer;

@ConfigurationProperties(prefix = "collector.kafka")
public record KafkaProperties(String bootstrapServers) {
    public static final Class<?> KEY_SERIALIZER_CLASS = StringSerializer.class;
    public static final Class<?> VALUE_SERIALIZER_CLASS = CollectorAvroSerializer.class;
}