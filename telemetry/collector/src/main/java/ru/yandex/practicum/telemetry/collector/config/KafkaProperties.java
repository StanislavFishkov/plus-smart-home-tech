package ru.yandex.practicum.telemetry.collector.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.yandex.practicum.telemetry.avro.AvroSerializer;

@ConfigurationProperties(prefix = "collector.kafka")
public record KafkaProperties(String bootstrapServers) {
    public static final Class<?> KEY_SERIALIZER_CLASS = StringSerializer.class;
    public static final Class<?> VALUE_SERIALIZER_CLASS = AvroSerializer.class;
}