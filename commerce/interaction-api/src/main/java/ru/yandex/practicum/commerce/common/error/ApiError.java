package ru.yandex.practicum.commerce.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.commerce.common.util.DateTimeUtil;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final String stackTrace;
    @JsonFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private final LocalDateTime timestamp = LocalDateTime.now();
}