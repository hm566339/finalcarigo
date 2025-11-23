package com.carigo.drivinglicense.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
@Profile("prod") // 🔥 Only active in Production
@Slf4j
public class RealExternalVehicleClient implements ExternalVehicleClient {

    private final WebClient webClient;

    @Value("${external.vehicle.api.url}")
    private String apiUrl;

    @Value("${external.vehicle.api.key}")
    private String apiKey;

    public RealExternalVehicleClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Override
    public Map<String, Object> fetchByNumber(String vehicleNumber) {

        try {
            log.info("🔍 Calling external vehicle API for number: {}", vehicleNumber);

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(apiUrl)
                            .queryParam("number", vehicleNumber)
                            .build())
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(
                            Retry.fixedDelay(2, Duration.ofSeconds(2))
                                    .filter(ex -> {
                                        log.warn("External API retry due to error: {}", ex.getMessage());
                                        return true;
                                    }))
                    .timeout(Duration.ofSeconds(8))
                    .block();

        } catch (Exception e) {
            log.error("❌ External API error: {}", e.getMessage());
            throw new RuntimeException("Vehicle Verification API failed: " + e.getMessage());
        }
    }
}
