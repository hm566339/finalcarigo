package com.hms.user.userms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
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
    AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/register")
                        .permitAll()
                        .requestMatchers(req -> req.getHeader("X-SECRET-KEY") != null &&
                                req.getHeader("X-SECRET-KEY").equals("SECRET"))
                        .authenticated()
                        .anyRequest().denyAll())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// package com.hms.user.userms.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import
// org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.authorization.AuthorizationDecision;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

// private final JwtAuthFilter jwtAuthFilter;

// public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
// this.jwtAuthFilter = jwtAuthFilter;
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// @Bean
// AuthenticationManager authenticationManager(AuthenticationConfiguration
// builder) throws Exception {
// return builder.getAuthenticationManager();
// }

// @Bean
// SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

// http
// .csrf(csrf -> csrf.disable())
// .formLogin(form -> form.disable())
// .httpBasic(basic -> basic.disable())
// .sessionManagement(sm ->
// sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

// .authorizeHttpRequests(auth -> auth

// // Public endpoints
// .requestMatchers("/user/login", "/user/register")
// .permitAll()

// // Internal APIs → require BOTH JWT + SECRET
// .requestMatchers("/users/**")
// .access((authentication, context) -> {

// var request = context.getRequest();

// String secret = request.getHeader("X-SECRET-KEY");
// boolean secretOK = "SECRET".equals(secret);

// var authObj = authentication.get();
// boolean jwtOK = authObj != null && authObj.isAuthenticated();

// return new AuthorizationDecision(secretOK && jwtOK);
// })

// // Block everything else
// .anyRequest().denyAll()

// ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }
// }
