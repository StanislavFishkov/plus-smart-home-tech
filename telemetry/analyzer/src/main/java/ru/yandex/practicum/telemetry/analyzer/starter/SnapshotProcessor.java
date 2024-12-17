package ru.yandex.practicum.telemetry.analyzer.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.AnalyzerTopics;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(AnalyzerTopics.class)
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final AnalyzerTopics analyzerTopics;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(analyzerTopics.sensorSnapshotsTopic()));

            // Poll Loop
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record);

                    manageOffsets(record, count, consumer);
                    count++;
                }
                // fix max offsets
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Analyzer: error during consuming loop from sensor snapshots topic", e);
        } finally {
            try {
                log.info("Analyzer: commit offsets before closing sensor snapshots consumer");
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Analyzer: close sensor snapshots consumer");
                consumer.close();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record, int count,
                               KafkaConsumer<String, SensorsSnapshotAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Analyzer: error during fixing offsets for sensor snapshots topic: {}", offsets, exception);
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorsSnapshotAvro> record) throws InterruptedException {
        log.info("Analyzer: record read. topic = {}, partition = {}, offset = {}, value: {}",
                record.topic(), record.partition(), record.offset(), record.value());

    }
}