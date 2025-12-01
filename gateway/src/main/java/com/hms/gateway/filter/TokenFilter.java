package com.hms.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.annotation.PostConstruct;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component("TokenFilter")
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private PublicKey publicKey;

    public TokenFilter() {
        super(Config.class);
    }

    @PostConstruct
    public void loadPublicKey() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
        String pem = new String(resource.getInputStream().readAllBytes());

        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(pem);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

        System.out.println("🔑 Public key loaded successfully in Gateway!");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getPath().toString();

            // PUBLIC API
            if (path.startsWith("/user/login") || path.startsWith("/user/register") || path.startsWith("/user/send-otp")
                    || path.startsWith("/user/verify-otp")) {
                return chain.filter(
                        exchange.mutate()
                                .request(r -> r.header("X-SECRET-KEY", "SECRET"))
                                .build());
            }

            // PRIVATE API — JWT REQUIRED
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(publicKey) // 🔥 NEW 0.12.5 METHOD
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                System.out.println("✅ JWT Verified: " + claims.getSubject());

            } catch (Exception e) {
                System.out.println("❌ Gateway JWT Error: " + e.getMessage());
                throw new RuntimeException("Invalid token: " + e.getMessage());
            }

            return chain.filter(
                    exchange.mutate()
                            .request(r -> r.header("X-SECRET-KEY", "SECRET"))
                            .build());
        };
    }

    public static class Config {
    }
}
