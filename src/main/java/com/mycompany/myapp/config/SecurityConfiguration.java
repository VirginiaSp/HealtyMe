package com.mycompany.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                    //.contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                    // H2 console needs frames enabled!
                    .frameOptions(f -> f.disable())
                    .permissionsPolicy(p ->
                        p.policy(
                            "accelerometer=(), camera=(), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), fullscreen=(self)"
                        )
                    )
            )
            .authorizeHttpRequests(auth ->
                auth
                    // Αυτή η γραμμή είναι ΚΡΙΣΙΜΗ:
                    .requestMatchers("/h2-console/**")
                    .permitAll()
                    .requestMatchers("/management/**", "/v3/api-docs/**", "/swagger-ui/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
