package ru.yandex.practicum.kafka.telemetry.collector.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    @PostMapping("/sensors")
    public void postSensor() {

    }

    @PostMapping("/hubs")
    public void postHub() {

    }

    @GetMapping("/sensors")
    public String getSensor() {
        return "Hello";
    }
}