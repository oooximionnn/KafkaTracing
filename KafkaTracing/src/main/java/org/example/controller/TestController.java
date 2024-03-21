package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductChangedEvent;
import org.example.service.KafkaSuperSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final KafkaSuperSender kafkaSuperSender;
    @GetMapping
    public void testKafka() {
        UUID key = UUID.randomUUID();

        ProductChangedEvent event = new ProductChangedEvent(
                key,
                key.toString(),
                "_____",
                "___",
                "____"
        );


        kafkaSuperSender.sendMessage(event, key);
    }
}
