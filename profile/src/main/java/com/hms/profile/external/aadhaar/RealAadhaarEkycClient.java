package com.hms.profile.external.aadhaar;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.hms.profile.external.AadhaarEkycClient;

import lombok.RequiredArgsConstructor;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class RealAadhaarEkycClient implements AadhaarEkycClient {

    private final WebClient.Builder builder;

    @Value("${aadhaar.api.base-url}")
    private String baseUrl;

    @Value("${aadhaar.api.client-id}")
    private String clientId;

    @Value("${aadhaar.api.secret}")
    private String clientSecret;

    @Override
    public Map<String, Object> fetchEkyc(String txnId) {
        return builder.build()
                .get()
                .uri(baseUrl + "/ekyc/fetch?txnId=" + txnId)
                .header("client-id", clientId)
                .header("client-secret", clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
