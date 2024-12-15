package ru.yandex.practicum.telemetry.collector.util;

import com.google.protobuf.Timestamp;

import java.time.Instant;

public final class TimestampProto {
    public static Instant toInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getSeconds());
    }

    public static long toEpochMilli(Timestamp timestamp) {
        return toInstant(timestamp).toEpochMilli();
    }
}
