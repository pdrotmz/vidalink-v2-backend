package com.vidalink.healthcare.entity.presentation.controller;

import com.vidalink.healthcare.identity.application.dto.response.MeResponse;
import com.vidalink.healthcare.identity.application.usecase.GetCurrentUserUseCase;
import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.shared.infrastructure.security.JwtAuthenticationFilter;
import com.vidalink.healthcare.identity.presentation.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // TODO: Verify this test
    @Test
    void shouldReturnCurrentUser() throws Exception {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Pedro");
        user.setEmail("pedro@gmail.com");
        user.setCpf("12345678900");
        user.setRole(UserRole.CLIENT);

        MeResponse response = new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        when(getCurrentUserUseCase.execute(any()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/users/me")
                                .with(authentication(
                                        new UsernamePasswordAuthenticationToken(
                                                user,
                                                null,
                                                user.getAuthorities()
                                        )
                                ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@gmail.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

        verify(getCurrentUserUseCase)
                .execute(any());
    }
}
