package com.carigo.booking.config;

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

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // http
    // .csrf(csrf -> csrf.disable())
    // .sessionManagement(sm ->
    // sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .formLogin(form -> form.disable())
    // .httpBasic(basic -> basic.disable())

    // .authorizeHttpRequests(auth -> auth

    // // 🔓 PUBLIC Routes (Registration) — No JWT Required
    // .requestMatchers(HttpMethod.POST, "/renters", "/car-owners")
    // .permitAll()

    // // 🔓 Internal Feign Calls Allowed
    // .requestMatchers(req -> {
    // String key = req.getHeader("X-SECRET-KEY");
    // return key != null && key.equals("SECRET");
    // }).permitAll()

    // // 🔐 All Profile operations require JWT
    // .requestMatchers(
    // "/car-owners/**",
    // "/renters/**",
    // "/kyc/**")
    // .authenticated()

    // // ❌ Block everything else
    // .anyRequest().denyAll())

    // // 🔐 JWT Filter must come before username/password filter
    // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    // return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // 🌍 PUBLIC APIs (Gateway only)
                        // .requestMatchers(
                        // "car-owners", "renters")
                        // .access((authentication, context) -> {
                        // String secret = context.getRequest().getHeader("X-SECRET-KEY");
                        // return new AuthorizationDecision("SECRET".equals(secret));
                        // })

                        // 🗑️ DELETE APIs — ADMIN + OWNER + REANT
                        .requestMatchers("/renter/bookings/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowedRoles = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_REANT"));

                            return new AuthorizationDecision(secretOK && allowedRoles);
                        })
                        .requestMatchers("/owner/bookings/**")
                        .access((authentication, context) -> {

                            String secret = context.getRequest().getHeader("X-SECRET-KEY");
                            boolean secretOK = "SECRET".equals(secret);

                            var authObj = authentication.get();
                            boolean allowedRoles = authObj != null &&
                                    authObj.isAuthenticated() &&
                                    authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

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
