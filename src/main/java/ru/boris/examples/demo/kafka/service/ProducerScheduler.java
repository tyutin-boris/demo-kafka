package ru.boris.examples.demo.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.boris.examples.demo.kafka.dto.DemoMessage;
import ru.boris.examples.demo.kafka.dto.DemoValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerScheduler {

    @Value("${application.kafka.topic}")
    private String topicName;

    private final KafkaTemplate<String, DemoValue> kafkaTemplate;

    private final MessageGenerator messageGenerator;

    @Scheduled(cron = "*/5 * * * * *")
    public void send() {
        log.info("Scheduler start.");

        List<DemoMessage> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            messages.add(messageGenerator.getMessage());
        }


        messages.forEach(message -> kafkaTemplate.send(topicName, message.getKey(), message.getValue())
                .whenComplete(getSendResultThrowableBiConsumer(message.getValue())));
    }

    private BiConsumer<SendResult<String, DemoValue>, Throwable> getSendResultThrowableBiConsumer(DemoValue value) {
        return (response, ex) -> {
            if (Objects.isNull(ex)) {
                log.info("Scheduler send message: " + value);
            } else {
                log.error("kafka return exception.", ex);
            }
        };
    }
}
