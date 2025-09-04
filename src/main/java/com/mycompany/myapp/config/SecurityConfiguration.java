package com.mycompany.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(h ->
                h
                    .frameOptions(f -> f.disable())
                    .permissionsPolicy(p ->
                        p.policy(
                            "accelerometer=(), camera=(), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), fullscreen=(self)"
                        )
                    )
            )
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/h2-console/**")
                    .permitAll()
                    .requestMatchers("/management/**", "/v3/api-docs/**", "/swagger-ui/**")
                    .permitAll()
                    .requestMatchers("/api/register")
                    .permitAll()
                    .requestMatchers("/api/authenticate")
                    .permitAll()
                    .requestMatchers("/api/activate")
                    .permitAll()
                    .requestMatchers("/api/account/reset-password/init")
                    .permitAll()
                    .requestMatchers("/api/account/reset-password/finish")
                    .permitAll()
                    .requestMatchers("/api/account/change-password")
                    .permitAll()
                    .requestMatchers("/api/places")
                    .permitAll() // <<< ΔΩΣΕ ΠΡΟΣΩΡΙΝΑ PERMIT ΓΙΑ TEST
                    .anyRequest()
                    .authenticated()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
