package ru.yandex.practicum.kafka.telemetry.collector.error.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
