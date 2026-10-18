
package com.santosh.aiproductionintelligence.service;

import com.santosh.aiproductionintelligence.entity.OutboxEvent;
import com.santosh.aiproductionintelligence.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OutboxPublisher {

    private static final String TOPIC = "incident.created";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(
            OutboxEventRepository outboxEventRepository,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        List<OutboxEvent> events =
                outboxEventRepository
                        .findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        TOPIC,
                        event.getAggregateId().toString(),
                        event.getPayload()
                ).get(10, TimeUnit.SECONDS);

                event.setPublishedAt(Instant.now());
                outboxEventRepository.save(event);

                System.out.println(
                        "Published outbox event: " + event.getId()
                );
            } catch (Exception e) {
                System.err.println(
                        "Failed to publish outbox event "
                                + event.getId() + ": " + e.getMessage()
                );
            }
        }
    }
}
