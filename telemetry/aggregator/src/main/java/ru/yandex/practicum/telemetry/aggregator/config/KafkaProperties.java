package ru.yandex.practicum.telemetry.aggregator.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.yandex.practicum.telemetry.avro.AvroSerializer;
import ru.yandex.practicum.telemetry.avro.SensorEventDeserializer;

@ConfigurationProperties(prefix = "aggregator.kafka")
public record KafkaProperties(String bootstrapServers, String consumerGroupId) {
    public static final Class<?> KEY_SERIALIZER_CLASS = StringSerializer.class;
    public static final Class<?> KEY_DESERIALIZER_CLASS = StringDeserializer.class;
    public static final Class<?> VALUE_SERIALIZER_CLASS = AvroSerializer.class;
    public static final Class<?> SENSOR_EVENT_DESERIALIZER_CLASS = SensorEventDeserializer.class;
}