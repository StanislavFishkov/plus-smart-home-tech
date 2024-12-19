package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor
public class KafkaClientsConfig {
    private final KafkaProperties kafkaProperties;

    @Bean("hubEventsConsumer")
    public KafkaConsumer<String, HubEventAvro> hubEventsConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaProperties.KEY_DESERIALIZER_CLASS);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProperties.HUB_EVENT_DESERIALIZER_CLASS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.hubsConsumerGroupId());

        return new KafkaConsumer<>(config);
    }

    @Bean("snapshotsConsumer")
    public KafkaConsumer<String, SensorsSnapshotAvro> snapshotsConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaProperties.KEY_DESERIALIZER_CLASS);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProperties.SNAPSHOT_DESERIALIZER_CLASS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.snapshotsConsumerGroupId());

        return new KafkaConsumer<>(config);
    }
}