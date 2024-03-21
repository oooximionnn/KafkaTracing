package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
@ComponentScan
@RequiredArgsConstructor
public class KafkaConfig {
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate(final ProducerFactory<Object, Object> producerFactory) {
        final KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }
}
