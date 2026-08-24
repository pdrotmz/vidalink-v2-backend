package com.vidalink.healthcare.entity.infrastructure.jwt;

import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "1234567890123456789012345678901234567890123456789012345678901234";

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                SECRET
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                3600000L
        );
    }

    @Test
    void shouldGenerateToken() {
        User user = new User();

        user.setEmail("pedro@gmail.com");
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsername() {
        User user = new User();

        user.setEmail("pedro@gmail.com");

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);

        assertEquals(
                "pedro@gmail.com",
                username
        );
    }

    @Test
    void shouldValidateToken() {

        User user = new User();

        user.setEmail("pedro@gmail.com");

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(
                token,
                user
        ));
    }

    @Test
    void shouldExpireToken() {
        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                SECRET
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                -1000L
        );

        User user = new User();

        user.setEmail("pedro@gmail.com");

        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenExpired(token));
    }
}
