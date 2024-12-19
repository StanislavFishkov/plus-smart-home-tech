package ru.yandex.practicum.telemetry.analyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analyzer.topic")
public record AnalyzerTopics(String hubEventsTopic, String sensorSnapshotsTopic) {
}