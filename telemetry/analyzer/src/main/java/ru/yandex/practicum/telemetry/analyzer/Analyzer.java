package ru.yandex.practicum.telemetry.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Analyzer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);

//        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
//        aggregator.start();
    }
}