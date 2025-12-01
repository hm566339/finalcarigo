package com.carigo.vehicle.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleFeignConfig {

    @Value("${external.vehicle.api.key}")
    private String apiKey;

    // Add API key header
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("X-API-KEY", apiKey);
        };
    }

    // Set timeouts
    @Bean
    public Request.Options timeoutConfig() {
        return new Request.Options(
                5000, // connect timeout
                5000 // read timeout
        );
    }

    // Feign logging (optional)
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
