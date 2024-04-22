package ru.boris.examples.demo.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.boris.examples.demo.kafka.dto.DemoValue;

import java.util.List;

@Slf4j
@Service
public class DemoListener {

    @KafkaListener(topics = "${application.kafka.topic}",
            containerFactory = "listenerContainerFactory")
    public void listen(List<Message<DemoValue>> messages) {

        log.info("Batch size: " + messages.size());

        for (Message<DemoValue> message : messages) {
            log.info("Received message with key: " + message.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            log.info("Received message: " + message.getPayload());
        }
    }
}
