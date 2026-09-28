package com.santosh.aiproductionintelligence.service;

import com.santosh.aiproductionintelligence.entity.Incident;
import com.santosh.aiproductionintelligence.repository.IncidentRepository;
import org.springframework.stereotype.Service;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Incident createIncident(Incident incident){

        return incidentRepository.save(incident);


    }
}
