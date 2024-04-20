package ru.boris.examples.demo.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.BiConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerSchedulerImpl implements ProducerScheduler {

    @Value("${application.kafka.topic}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Scheduled(cron = "*/10 * * * * *")
    public void send() {
        log.info("Scheduler start.");

        String helloKafka = "Hello kafka";

        kafkaTemplate.send(topicName, helloKafka)
                .whenComplete(getSendResultThrowableBiConsumer(helloKafka));
    }

    private BiConsumer<SendResult<String, String>, Throwable> getSendResultThrowableBiConsumer(String helloKafka) {
        return (response, ex) -> {
            if (Objects.isNull(ex)) {
                log.info("Scheduler send message: " + helloKafka);
            } else {
                log.error("kafka return exception.", ex);
            }
        };
    }
}
