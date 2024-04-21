package ru.boris.examples.demo.kafka.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.boris.examples.demo.kafka.dto.DemoValue;

import java.util.Map;

@Configuration
public class MyProducerConfig {

    private final String demoTopic;

    public MyProducerConfig(@Value("${application.kafka.topic}") String demoTopic) {
        this.demoTopic = demoTopic;
    }

    @Bean
    public ProducerFactory<String, DemoValue> producerFactory(KafkaProperties kafkaProperties,
                                                              ObjectMapper objectMapper) {
        Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties(null);

        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory<String, DemoValue> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProperties);
        producerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));

        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, DemoValue> kafkaTemplate(ProducerFactory<String, DemoValue> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic demoTopic() {
        return TopicBuilder.name(demoTopic).partitions(1).replicas(1).build();
    }
}
