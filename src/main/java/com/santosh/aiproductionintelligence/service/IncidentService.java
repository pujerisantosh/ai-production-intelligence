package com.santosh.aiproductionintelligence.service;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.santosh.aiproductionintelligence.entity.Incident;
import com.santosh.aiproductionintelligence.entity.OutboxEvent;
import com.santosh.aiproductionintelligence.repository.IncidentRepository;
import com.santosh.aiproductionintelligence.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public IncidentService(
            IncidentRepository incidentRepository,
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper) {
        this.incidentRepository = incidentRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Incident createIncident(Incident incident) {

        Incident savedIncident = incidentRepository.save(incident);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateId(savedIncident.getId());
        outboxEvent.setAggregateType("INCIDENT");
        outboxEvent.setEventType("INCIDENT_CREATED");

        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(savedIncident));
        } catch (JacksonException  e) {
            throw new IllegalStateException("Failed to create incident event payload", e);
        }

        outboxEventRepository.save(outboxEvent);

        return savedIncident;
    }

    public List<OutboxEvent> getPendingOutboxEvents() {
        return outboxEventRepository
                .findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
    }
}