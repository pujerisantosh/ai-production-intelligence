package com.santosh.aiproductionintelligence.dto;

import java.time.Instant;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        String serviceName,
        String severity,
        String description,
        String status,
        Instant createdAt
) {
}