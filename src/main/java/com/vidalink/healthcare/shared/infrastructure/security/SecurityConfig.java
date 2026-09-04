package com.vidalink.healthcare.shared.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
                .cors(Customizer.withDefaults())
                .headers(headers ->
                        headers.frameOptions(frame ->
                                frame.sameOrigin()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",

                                "/h2-console/**",

                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        //ADMIN - Users
                        .requestMatchers(
                                "/api/users/"
                        ).hasRole("ADMIN")

                        // ADMIN - Rewards
                        .requestMatchers(
                                "/api/rewards/create",
                                "/api/rewards/update/{id}",
                                "/api/rewards/id/{id}/deactivate"
                        ).hasRole("ADMIN")

                        // AUTHENTICATED - Rewards
                        .requestMatchers(
                                "/api/rewards/",
                                "/api/rewards/id/{id}",
                                "/api/rewards/name/{name}",
                                "/api/rewards/search",
                                "/api/rewards/id/{id}/image"
                        ).authenticated()

                        // AUTHENTICATED - Redemptions
                        .requestMatchers(
                                "/api/redemptions/redeem",
                                "/api/redemptions",
                                "/api/redemptions/id/{id}",
                                "/api/redemptions/user/id/{id}",
                                "/api/redemptions/reward/id/{id}"
                        ).authenticated()

                        // AUTHENTICATED - Submissions
                        .requestMatchers(
                                "/api/submissions/send",
                                "/api/submissions",
                                "/api/submissions/id/{id}",
                                "/api/submissions/id/user/{id}"
                        ).authenticated()

                        // ADMIN - Submission management
                        .requestMatchers(
                                "/api/submissions/status",
                                "/api/submissions/id/status/{id}/approve",
                                "/api/submissions/id/status/{id}/reject",
                                "/api/submissions/id/delete/{id}"
                        ).hasRole("ADMIN")

                        // AUTHENTICATED - User
                        .requestMatchers(
                                "/api/users/me"
                        ).authenticated()

                        // AUTHENTICATED - Gamification
                        .requestMatchers(
                                "/api/points/me",
                                "/api/points/me/level",
                                "/api/points/me/badges",
                                "/api/points/{userId}/badges",
                                "/api/points/me/transactions"
                        ).authenticated()

                        // ADMIN - Gamification management
                        .requestMatchers(
                                "/api/points/transactions"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
