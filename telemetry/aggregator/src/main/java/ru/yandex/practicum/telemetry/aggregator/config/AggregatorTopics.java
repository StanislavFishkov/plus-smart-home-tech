package ru.yandex.practicum.telemetry.aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aggregator.topic")
public record AggregatorTopics(String sensorEventsTopic, String sensorSnapshotsTopic) {
}