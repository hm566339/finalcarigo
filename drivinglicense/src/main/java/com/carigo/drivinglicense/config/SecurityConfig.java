package com.carigo.drivinglicense.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true) // 🔥 Needed for @RolesAllowed
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/dl/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowedRoles = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") ||
                                                    a.getAuthority().equals("ROLE_OWNER") ||
                                                    a.getAuthority().equals("ROLE_REANT"));

                            return new AuthorizationDecision(secretOK && allowedRoles);
                        })

                        // 👑 ALL OTHER APIs — ONLY ADMIN
                        .anyRequest()
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean isAdmin = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            return new AuthorizationDecision(secretOK && isAdmin);
                        }))

                // 🔐 JWT FILTER
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
