package com.huce.project.healcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExternalServiceHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;


    @Override
    public Health health() {
        try {
            // Kiểm tra service bên ngoài
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://team2.site//health", String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Health.up()
                        .withDetail("external-service", "Service is running")
                        .build();
            } else {
                return Health.down()
                        .withDetail("external-service", "Service returned: " +
                                response.getStatusCode())
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("external-service", "Service is not available")
                    .withException(e)
                    .build();
        }
    }
}