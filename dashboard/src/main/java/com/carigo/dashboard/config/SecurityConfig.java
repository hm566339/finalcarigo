package com.carigo.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableMethodSecurity(jsr250Enabled = true)
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

                        // 🟢 OWNER DASHBOARD
                        .requestMatchers("/dashboard/owner/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowed = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

                            return new AuthorizationDecision(secretOK && allowed);
                        })

                        // 🟢 RENTER DASHBOARD
                        .requestMatchers("/dashboard/renter/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowed = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_REANT"));

                            return new AuthorizationDecision(secretOK && allowed);
                        })

                        // 🟢 ADMIN DASHBOARD
                        .requestMatchers("/dashboard/admin/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowed = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            return new AuthorizationDecision(secretOK && allowed);
                        })

                        // ❌ BLOCK ANY OTHER DASHBOARD PATH
                        .requestMatchers("/dashboard/**")
                        .denyAll()

                        // ❌ BLOCK EVERYTHING ELSE
                        .anyRequest().denyAll())

                // 🔐 JWT FILTER (VERY IMPORTANT)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
