package com.hms.user.userms.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private static final Long JWT_TOKEN_VALIDITY = 5 * 60 * 60L;

    private static final String SECRET_KEY = "WN79UZ7T9DCNF90HEQD8AJAENT4YTACEPV57H0S922NTTUCNTQFF6WAGU82NNZ5WWSN215NGFIBZ2TWX9W7OSPTZQ30843E6WD0EVCMHGBHGKUQ23ZDPKEJUT7LVJMQW2OM7VN7MQJQZ4ZAAVV33GT";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        CustomUserDetail user = (CustomUserDetail) userDetails;
        claims.put("id", user.getId());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        return doGenerateToken(claims, user.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }
}
