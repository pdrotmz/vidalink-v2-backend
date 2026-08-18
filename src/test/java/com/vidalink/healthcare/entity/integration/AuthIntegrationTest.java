package com.vidalink.healthcare.entity.integration;

import com.vidalink.healthcare.identity.application.dto.request.LoginRequest;
import com.vidalink.healthcare.identity.application.dto.request.RegisterRequest;
import com.vidalink.healthcare.identity.infrastructure.persistence.jpa.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void shouldRegisterUserInPostgres() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertTrue(userRepository.existsByEmail("pedro@gmail.com"));
    }

    @Test
    void shouldNotRegisterUserWIthExistingEmail() throws Exception {
        RegisterRequest firstRequest = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "123456789"
        );

        RegisterRequest secondRequest = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "123456789"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest))
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest))
        )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotRegisterUserWithExistingCpf() throws Exception {
        RegisterRequest firstRequest = new RegisterRequest(
                "Pedro",
                "pedro1@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        RegisterRequest secondRequest = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest))
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest))
        )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldLogin() throws Exception {

        RegisterRequest register = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        )
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest(
                "pedro@gmail.com",
                "pedrotomaz123"
        );

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldRejectInvalidPassword() throws Exception {

        RegisterRequest register = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrtomaz123",
                "12345678900"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        );

        LoginRequest login = new LoginRequest(
                "pedro@gmail.com",
                "wrongPassword"
        );

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        RegisterRequest register = new RegisterRequest(
                "Pedro",
                "pedro@gmail.com",
                "pedrotomaz123",
                "12345678900"
        );

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
        )
                .andExpect(status().isCreated());


        LoginRequest login = new LoginRequest(
                "pedrotomaz@gmail.com",
                "pedrotomaz123"
        );

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidJwt() throws Exception {

        mockMvc.perform(
                get("/api/users/me")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }
}
