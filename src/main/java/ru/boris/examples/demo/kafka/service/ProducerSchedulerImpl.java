package ru.boris.examples.demo.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerSchedulerImpl implements ProducerScheduler {

    @Override
    @Scheduled(cron = "*/10 * * * * *")
    public void send() {
        log.info("Scheduler start.");
    }
}
