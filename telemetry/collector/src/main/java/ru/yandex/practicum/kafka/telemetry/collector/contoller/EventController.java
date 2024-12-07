package ru.yandex.practicum.kafka.telemetry.collector.contoller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.kafka.telemetry.collector.dto.sensor.SensorEvent;

@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        // ... реализация метода ...
    }

    @PostMapping("/sensors")
    public void collectHubEvent(@Valid @RequestBody SensorEvent event) {
        // ... реализация метода ...
    }

    @GetMapping("/sensors")
    public String getSensor() {
        return "Hello";
    }
}