package com.santosh.aiproductionintelligence.controller;

import com.santosh.aiproductionintelligence.dto.CreateIncidentRequest;
import com.santosh.aiproductionintelligence.dto.IncidentResponse;
import com.santosh.aiproductionintelligence.entity.Incident;
import com.santosh.aiproductionintelligence.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse createIncident(
            @Valid @RequestBody CreateIncidentRequest request) {

        Incident incident = new Incident();
        incident.setServiceName(request.serviceName());
        incident.setSeverity(request.severity());
        incident.setDescription(request.description());
        incident.setStatus("OPEN");

        Incident savedIncident = incidentService.createIncident(incident);

        return new IncidentResponse(
                savedIncident.getId(),
                savedIncident.getServiceName(),
                savedIncident.getSeverity(),
                savedIncident.getDescription(),
                savedIncident.getStatus(),
                savedIncident.getCreatedAt()
        );
    }
}