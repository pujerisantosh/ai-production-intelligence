package com.santosh.aiproductionintelligence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateIncidentRequest(

        @NotBlank
        String serviceName,

        @NotBlank
        String severity,

        @NotBlank
        @Size(max = 2000)
        String description
) {
}