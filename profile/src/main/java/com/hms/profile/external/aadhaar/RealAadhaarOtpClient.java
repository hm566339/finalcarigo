package com.hms.profile.external.aadhaar;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.hms.profile.external.AadhaarOtpClient;

import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
@Profile("prod") // Only active in PRODUCTION
@RequiredArgsConstructor
public class RealAadhaarOtpClient implements AadhaarOtpClient {

    private final WebClient.Builder builder;

    @Value("${aadhaar.api.base-url}")
    private String baseUrl;

    @Value("${aadhaar.api.client-id}")
    private String clientId;

    @Value("${aadhaar.api.secret}")
    private String clientSecret;

    @Override
    public Map<String, Object> generateOtp(String aadhaar) {
        return builder.build()
                .post()
                .uri(baseUrl + "/otp/generate")
                .header("client-id", clientId)
                .header("client-secret", clientSecret)
                .bodyValue(Map.of("aadhaar", aadhaar))
                .retrieve()
                .bodyToMono(Map.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                .block();
    }

    @Override
    public Map<String, Object> verifyOtp(String txnId, String otp) {
        return builder.build()
                .post()
                .uri(baseUrl + "/otp/verify")
                .header("client-id", clientId)
                .header("client-secret", clientSecret)
                .bodyValue(Map.of("txnId", txnId, "otp", otp))
                .retrieve()
                .bodyToMono(Map.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(2)))
                .block();
    }
}
