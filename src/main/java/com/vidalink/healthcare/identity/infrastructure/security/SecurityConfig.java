package com.vidalink.healthcare.identity.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    AuthenticationProvider authenticationProvider(
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {

        http.
                csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(frame ->
                                frame.sameOrigin()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(

                                // Auth endpoints
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/users/me",

                                // H2 endpoints
                                "/h2-console/**",

                                // Reward endpoints
                                "/api/rewards/create",
                                "/api/rewards",
                                "/api/rewards/id/{id}",
                                "/api/rewards/name/{name}",
                                "/api/rewards/search",
                                "/api/rewards/update/{id}",
                                "/api/rewards/id/{id}/deactivate",

                                // Redemption endpoints
                                "/api/redemptions/redeem",
                                "/api/redemptions",
                                "/api/redemptions/id/{id}",
                                "/api/redemptions/user/id/{id}",
                                "/api/redemptions/reward/id/{id}",

                                // Submission endpoints
                                "/api/submission/send",
                                "/api/submissions",
                                "/api/submissions/id/{id}",
                                "/api/submissions/id/user/{id}",
                                "/api/submissions/id/status/{id}/approve",
                                "/api/submissions/id/status/{id}/reject",
                                "/api/submissions/id/delete/{id}"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
