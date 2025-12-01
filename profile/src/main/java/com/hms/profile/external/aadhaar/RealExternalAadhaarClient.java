package com.hms.profile.external.aadhaar;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.hms.profile.external.ExternalAadhaarClient;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class RealExternalAadhaarClient implements ExternalAadhaarClient {

    private final WebClient.Builder builder;

    @Value("${aadhaar.api.base-url}")
    private String baseUrl;

    @Value("${aadhaar.api.client-id}")
    private String clientId;

    @Value("${aadhaar.api.secret}")
    private String clientSecret;

    @Override
    public Map<String, Object> verifyAadhaar(String aadhaarNumber) {
        return builder.build()
                .post()
                .uri(baseUrl + "/verify")
                .header("client-id", clientId)
                .header("client-secret", clientSecret)
                .bodyValue(Map.of("aadhaar", aadhaarNumber))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
