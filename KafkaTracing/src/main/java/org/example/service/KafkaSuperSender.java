package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSuperSender {

    private final KafkaTemplate kafkaTemplate;

    @Async
    public void sendMessage(Object message, UUID key) {
        sendMessage(message,key.toString());
    }

    @Async
    protected void sendMessage(Object message, String key) {

        kafkaTemplate.send("testtopic", key, message)
                .thenAccept(result -> {
                    log.info("producerListener -> Message was sent. msg: {}", result);
                })
                .exceptionally(ex -> {
                    log.error("producerListener -> Error: {}", message, ex);
                    return null;
                });
    }
}
