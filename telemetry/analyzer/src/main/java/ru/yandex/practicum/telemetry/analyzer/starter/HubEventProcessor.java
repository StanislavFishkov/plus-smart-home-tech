package ru.yandex.practicum.telemetry.analyzer.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    // ...

    @Override
    public void run() {
        // подписка на топики
        // ...
        // цикл опроса
    }

    // ...детали реализации...
}