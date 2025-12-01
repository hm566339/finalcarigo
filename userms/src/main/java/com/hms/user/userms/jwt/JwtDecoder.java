package com.hms.user.userms.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtDecoder {

    private final PublicKey publicKey;

    public JwtDecoder() {
        try {
            ClassPathResource resource = new ClassPathResource("keys/public.pem");
            String keyContent = new String(resource.getInputStream().readAllBytes());

            keyContent = keyContent
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            // 🔥 IMPORTANT: MIME decoder (correct for PEM)
            byte[] keyBytes = Base64.getMimeDecoder().decode(keyContent);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(cleanToken(token))
                .getPayload()
                .get("role", String.class);
    }

    public Long extractId(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(cleanToken(token))
                .getPayload()
                .get("id", Long.class);
    }

    private String cleanToken(String token) {
        if (token == null)
            throw new RuntimeException("Token is null");
        return token.replace("Bearer ", "").trim();
    }
}
