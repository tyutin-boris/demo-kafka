package ru.boris.examples.demo.kafka.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.boris.examples.demo.kafka.dto.DemoValue;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class MyConsumerConfig {

    private final String demoTopic;

    public MyConsumerConfig(@Value("${application.kafka.topic}") String demoTopic) {
        this.demoTopic = demoTopic;
    }

    @Bean
    public ConsumerFactory<String, DemoValue> consumerFactory(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        String value = "ru.boris.examples.demo.kafka.dto.DemoValue:ru.boris.examples.demo.kafka.dto.DemoValue";

        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties(null);

        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        consumerProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_000);
        consumerProperties.put(JsonDeserializer.TYPE_MAPPINGS, value);

        DefaultKafkaConsumerFactory<String, DemoValue> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);
        consumer.setValueDeserializer(new JsonDeserializer<>(objectMapper));
        return consumer;
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, DemoValue>>
    listenerContainerFactory(ConsumerFactory<String, DemoValue> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, DemoValue> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);

        ContainerProperties containerProperties = factory.getContainerProperties();
        containerProperties.setIdleBetweenPolls(1_000);
        containerProperties.setPollTimeout(1_000);

        ExecutorService executor = Executors.newFixedThreadPool(1, task -> new Thread(task, "kafka-consumer"));

        ConcurrentTaskExecutor concurrentTaskExecutor = new ConcurrentTaskExecutor(executor);
        containerProperties.setListenerTaskExecutor(concurrentTaskExecutor);

        return factory;
    }
}
