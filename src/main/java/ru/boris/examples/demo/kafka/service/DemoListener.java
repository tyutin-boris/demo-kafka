package ru.boris.examples.demo.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DemoListener {

    @KafkaListener(topics = "${application.kafka.topic}",
            containerFactory = "listenerContainerFactory",
            properties = "{auto.offset.reset=earliest}")
    public void listen(@Payload List<String> messages) {
        log.info("Batch size: " + messages.size());

        for (String message : messages) {
            log.info("Received message: " + message);
        }
    }
}
