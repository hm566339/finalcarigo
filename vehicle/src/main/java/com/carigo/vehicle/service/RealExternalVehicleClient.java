package com.carigo.vehicle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
@Profile("prod") // 🔥 Active only in Production
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
            return webClient.get()
                    .uri(apiUrl + "?number=" + vehicleNumber)
                    .header("X-API-KEY", apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(
                            Retry.fixedDelay(2, Duration.ofSeconds(2)))
                    .timeout(Duration.ofSeconds(5))
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("External API failed: " + e.getMessage());
        }
    }
}
