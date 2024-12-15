package ru.yandex.practicum.telemetry.aggregator.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.config.AggregatorTopics;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaProperties;
import ru.yandex.practicum.telemetry.aggregator.service.SnapshotService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@EnableConfigurationProperties({AggregatorTopics.class, KafkaProperties.class})
@RequiredArgsConstructor
public class AggregationStarter {
    // ... объявление полей и конструктора ...
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final AggregatorTopics aggregatorTopics;
    private final SnapshotService snapshotService;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        // регистрируем хук, в котором вызываем метод wakeup.
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            // подписываемся на топики
            consumer.subscribe(List.of(aggregatorTopics.sensorEventsTopic()));

            // начинаем Poll Loop
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record);
                    // фиксируем оффсеты обработанных записей, если нужно
                    manageOffsets(record, count, consumer);
                    count++;
                }
                // фиксируем максимальный оффсет обработанных записей
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедиться,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                // здесь нужно вызвать метод консьюмера для фиксиции смещений
                log.info("Коммитим оффсеты при закрытии синхронно.");
                consumer.commitSync(currentOffsets);
                log.info("Отправляем имеющиеся сообщения из очереди продюсера в брокер.");
                producer.flush();

            } finally {
                log.info("Закрываем продюсер");
                producer.close();
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count,
                                      KafkaConsumer<String, SensorEventAvro> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) throws InterruptedException {
        log.info("топик = {}, партиция = {}, смещение = {}, значение: ",
                record.topic(), record.partition(), record.offset(), record.value());

        Optional<SensorsSnapshotAvro> snapshot = snapshotService.updateState(record.value());
        if (snapshot.isPresent()) {
            SensorsSnapshotAvro snapshotAvro = snapshot.get();
            ProducerRecord<String, SensorsSnapshotAvro> snapshotRecord =
                    new ProducerRecord<>(aggregatorTopics.sensorSnapshotsTopic(), null,
                            snapshotAvro.getTimestamp().toEpochMilli(), snapshotAvro.getHubId(), snapshotAvro);

            producer.send(snapshotRecord);
            log.info("Kafka producer sent message {}\n", snapshotRecord);
        }
    }
}