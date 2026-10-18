package com.santosh.aiproductionintelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AiProductionIntelligenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiProductionIntelligenceApplication.class, args);
    }

}
