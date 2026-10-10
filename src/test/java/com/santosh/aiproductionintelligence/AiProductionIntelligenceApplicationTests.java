
package com.santosh.aiproductionintelligence;

import com.santosh.aiproductionintelligence.entity.Incident;
import com.santosh.aiproductionintelligence.repository.IncidentRepository;
import com.santosh.aiproductionintelligence.repository.OutboxEventRepository;
import com.santosh.aiproductionintelligence.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.santosh.aiproductionintelligence.entity.OutboxEvent;

import static org.mockito.Mockito.reset;

@SpringBootTest
class AiProductionIntelligenceApplicationTests {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private IncidentRepository incidentRepository;

    @MockitoSpyBean
    private OutboxEventRepository outboxEventRepository;

    @Test
    void contextLoads() {
    }


    @Test
    void shouldRollbackIncidentWhenOutboxSaveFails() {
        long incidentsBefore = incidentRepository.count();
        long outboxEventsBefore = outboxEventRepository.count();

        Incident incident = new Incident();
        incident.setServiceName("payment-service");
        incident.setSeverity("HIGH");
        incident.setDescription("Testing rollback");
        incident.setStatus("OPEN");

        try {
            doThrow(new RuntimeException("Simulated outbox failure"))
                    .when(outboxEventRepository)
                    .save(any(OutboxEvent.class));

            assertThrows(
                    RuntimeException.class,
                    () -> incidentService.createIncident(incident)
            );

            assertEquals(
                    incidentsBefore,
                    incidentRepository.count(),
                    "Incident should roll back when outbox save fails"
            );

            assertEquals(
                    outboxEventsBefore,
                    outboxEventRepository.count(),
                    "Outbox event count should remain unchanged"
            );
        } finally {
            reset(outboxEventRepository);
        }
    }

}
