package com.carigo.notification.service.lmp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.carigo.notification.service.SmsProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "notification.sms", name = "provider", havingValue = "MSG91")
public class Msg91SmsProvider implements SmsProvider {

    private final WebClient.Builder webClientBuilder;

    @Value("${notification.sms.msg91.api-key}")
    private String apiKey;

    @Value("${notification.sms.msg91.sender-id}")
    private String senderId;

    @Value("${notification.sms.msg91.route}")
    private String route;

    @Override
    public void sendSms(String phone, String message) {
        try {
            WebClient client = webClientBuilder.build();

            client.post()
                    .uri("https://api.msg91.com/api/v5/flow/")
                    .header("authkey", apiKey)
                    .bodyValue(Map.of(
                            "sender", senderId,
                            "route", route,
                            "mobiles", phone,
                            "message", message))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("📩 SMS sent to " + phone);
        } catch (Exception e) {
            System.out.println("❌ SMS error: " + e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}
