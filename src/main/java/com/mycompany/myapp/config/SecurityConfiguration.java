// COMPLETE SecurityConfiguration.java with JWT support
package com.mycompany.myapp.config;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtSecret;

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
            // 🔥 ADD THIS: JWT Configuration for token validation
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
            // 🔥 ADD THIS: Stateless session management
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            );
        return http.build();
    }

    // 🔥 ADD THIS: JWT Decoder Bean
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
