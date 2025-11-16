package com.hms.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class TokenFilter extends AbstractGatewayFilterFactory<TokenFilter.Config> {

    private static final String SECRET_KEY = "WN79UZ7T9DCNF90HEQD8AJAENT4YTACEPV57H0S922NTTUCNTQFF6WAGU82NNZ5WWSN215NGFIBZ2TWX9W7OSPTZQ30843E6WD0EVCMHGBHGKUQ23ZDPKEJUT7LVJMQW2OM7VN7MQJQZ4ZAAVV33GT";

    public TokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // String path = exchange.getRequest().getURI().getPath();
            String path = exchange.getRequest().getPath().toString();
            if (path.equals("/user/login") || path.equals("/user/register")) {
                return chain.filter(exchange.mutate().request(r -> r.header("X-SECRET-KEY", "SECRET")).build());
            }
            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing Authorization header");
            }
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid Authorization header");
            }
            String token = authHeader.substring(7);
            // Here you can add logic to validate the token
            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
                exchange = exchange.mutate().request(r -> r.header("X-SECRET-KEY", "SECRET")).build();
            } catch (Exception e) {
                throw new RuntimeException("Invalid token");
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }

}
