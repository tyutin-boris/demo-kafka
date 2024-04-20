package ru.boris.examples.demo.kafka.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class MyProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties,
                                                           ObjectMapper objectMapper) {
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties(null);

        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties);
        producerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));

        return producerFactory;
    }
}
