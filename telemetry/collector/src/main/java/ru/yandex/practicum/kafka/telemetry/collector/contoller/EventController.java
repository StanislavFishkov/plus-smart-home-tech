package ru.yandex.practicum.kafka.telemetry.collector.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.kafka.telemetry.collector.dto.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.service.EventService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("POST /events/sensors with body: {}", event);
        eventService.collectSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("POST /events/hubs with body: {}", event);
        eventService.collectHubEvent(event);
    }

    @GetMapping("/sensors")
    public String getSensor() {
        return "Hello";
    }
}