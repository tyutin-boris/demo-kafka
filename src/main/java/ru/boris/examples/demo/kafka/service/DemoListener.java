package ru.boris.examples.demo.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoListener {

    @KafkaListener(topics = "${application.kafka.topic}")
    public void listen(String message) {
        log.info("Received message: " + message);
    }
}
