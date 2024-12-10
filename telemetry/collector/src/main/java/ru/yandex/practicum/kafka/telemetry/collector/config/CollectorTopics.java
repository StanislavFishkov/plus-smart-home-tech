package ru.yandex.practicum.kafka.telemetry.collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "collector.topic")
public record CollectorTopics(String sensorEventsTopic, String hubEventsTopic) {
}