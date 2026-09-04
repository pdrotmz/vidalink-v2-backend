package com.vidalink.healthcare.marketplace.presentation.controller;

import com.vidalink.healthcare.identity.domain.enums.UserRole;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.application.usecase.redemption.*;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.*;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import com.vidalink.healthcare.marketplace.presentation.controller.redemption.RedemptionController;
import com.vidalink.healthcare.shared.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedemptionController.class)
class RedemptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private CreateRedemptionUseCaseImpl createRedemptionUseCase;

    @MockitoBean
    private GetAllRedemptionsUseCaseImpl getAllRedemptionsUseCase;

    @MockitoBean
    private GetRedemptionByIdUseCaseImpl getRedemptionByIdUseCase;

    @MockitoBean
    private GetRedemptionByIdUserUseCaseImpl getRedemptionByIdUserUseCase;

    @MockitoBean
    private GetRedemptionByIdRewardUseCaseImpl getRedemptionByIdRewardUseCase;


    @Test
    void shouldCreateRedemptionSuccessfully() throws Exception {

        UUID redemptionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("user@gmail.com");
        user.setPassword("123456");

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        rewardId,
                        2
                );

        RedemptionResponse response =
                new RedemptionResponse(
                        redemptionId,
                        userId,
                        rewardId,
                        2,
                        LocalDateTime.now()
                );

        when(createRedemptionUseCase.execute(
                eq(userId),
                any(CreateRedemptionRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/redemptions/redeem")
                                .with(authenticatedUser(userId))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id")
                        .value(redemptionId.toString()))
                .andExpect(jsonPath("$.idUser")
                        .value(userId.toString()))
                .andExpect(jsonPath("$.idReward")
                        .value(rewardId.toString()))
                .andExpect(jsonPath("$.quantity")
                        .value(2));

        verify(createRedemptionUseCase).execute(
                eq(userId),
                any(CreateRedemptionRequest.class)
        );
    }

    @Test
    void shouldReturnBadRequestWhenRewardHasInsufficientStock() throws Exception {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("user@gmail.com");
        user.setPassword("123456");

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        UUID.randomUUID(),
                        10
                );

        when(createRedemptionUseCase.execute(
                eq(userId),
                any(CreateRedemptionRequest.class)
        )).thenThrow(
                new RewardInsufficientStockException(
                        "Insufficient reward stock"
                )
        );

        mockMvc.perform(
                        post("/api/redemptions/redeem")
                                .with(authenticatedUser(userId))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Insufficient reward stock"));
    }

    @Test
    void shouldReturnBadRequestWhenRedemptionQuantityIsInvalid() throws Exception {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("user@gmail.com");
        user.setPassword("123456");

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        UUID.randomUUID(),
                        0
                );

        when(createRedemptionUseCase.execute(
                eq(userId),
                any(CreateRedemptionRequest.class)
        )).thenThrow(
                new RedemptionQuanityUnderThanZeroException(
                        "Redemption quantity must be greater than zero"
                )
        );

        mockMvc.perform(
                        post("/api/redemptions/redeem")
                                .with(authenticatedUser(userId))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByUserIdIsNotFound() throws Exception {

        UUID userId = UUID.randomUUID();

        when(getRedemptionByIdUserUseCase.execute(userId))
                .thenThrow(
                        new RedemptionNotFoundByIdUserException(
                                "Redemption not found with id user: " + userId
                        )
                );

        mockMvc.perform(
                        get("/api/redemptions/user/id/{id}", userId)
                                .with(authenticatedUserId())

                )

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Redemption not found with id user: "
                                                + userId
                                )
                );

        verify(getRedemptionByIdUserUseCase)
                .execute(userId);
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByIdIsNotFound() throws Exception {

        UUID redemptionId = UUID.randomUUID();

        when(getRedemptionByIdUseCase.execute(redemptionId))
                .thenThrow(
                        new RedemptionNotFoundByIdException(
                                "Redemption not found with id: " + redemptionId
                        )
                );

        mockMvc.perform(
                        get("/api/redemptions/id/{id}", redemptionId)
                                .with(authenticatedUserId())

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Redemption not found with id: "
                                                + redemptionId
                                )
                );

        verify(getRedemptionByIdUseCase)
                .execute(redemptionId);
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByRewardIdIsNotFound() throws Exception {

        UUID rewardId = UUID.randomUUID();

        when(getRedemptionByIdRewardUseCase.execute(rewardId))
                .thenThrow(
                        new RedemptionNotFoundByIdRewardException(
                                "Redemption not found with id reward: " + rewardId
                        )
                );

        mockMvc.perform(
                        get("/api/redemptions/reward/id/{id}", rewardId)
                                .with(authenticatedUserId())

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Redemption not found with id reward: "
                                                + rewardId
                                )
                );

        verify(getRedemptionByIdRewardUseCase)
                .execute(rewardId);
    }

    @Test
    void shouldReturnAllRedemptions() throws Exception {

        RedemptionResponse redemption1 =
                new RedemptionResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        1,
                        LocalDateTime.now()
                );

        RedemptionResponse redemption2 =
                new RedemptionResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        2,
                        LocalDateTime.now()
                );

        when(getAllRedemptionsUseCase.execute())
                .thenReturn(List.of(redemption1, redemption2));

        mockMvc.perform(
                        get("/api/redemptions")
                                .with(authenticatedUserId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(getAllRedemptionsUseCase).execute();
    }

    @Test
    void shouldReturnRedemptionById() throws Exception {

        UUID redemptionId = UUID.randomUUID();

        RedemptionResponse response =
                new RedemptionResponse(
                        redemptionId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        2,
                        LocalDateTime.now()
                );

        when(getRedemptionByIdUseCase.execute(redemptionId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/redemptions/id/{id}", redemptionId)
                                .with(authenticatedUserId())
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id")
                        .value(redemptionId.toString()));

        verify(getRedemptionByIdUseCase)
                .execute(redemptionId);
    }

    @Test
    void shouldReturnRedemptionByUserId() throws Exception {

        UUID userId = UUID.randomUUID();

        RedemptionResponse response =
                new RedemptionResponse(
                        UUID.randomUUID(),
                        userId,
                        UUID.randomUUID(),
                        2,
                        LocalDateTime.now()
                );

        when(getRedemptionByIdUserUseCase.execute(userId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/redemptions/user/id/{id}", userId)
                                .with(authenticatedUserId())
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.idUser")
                        .value(userId.toString()));

        verify(getRedemptionByIdUserUseCase)
                .execute(userId);
    }

    @Test
    void shouldReturnRedemptionByRewardId() throws Exception {

        UUID rewardId = UUID.randomUUID();

        RedemptionResponse response =
                new RedemptionResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        rewardId,
                        2,
                        LocalDateTime.now()
                );

        when(getRedemptionByIdRewardUseCase.execute(rewardId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/redemptions/reward/id/{id}", rewardId)
                                .with(authenticatedUserId())
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.idReward")
                        .value(rewardId.toString()));

        verify(getRedemptionByIdRewardUseCase)
                .execute(rewardId);
    }

    private RequestPostProcessor authenticatedUser(UUID userId) {

        User user = new User();
        user.setId(userId);
        user.setEmail("user@gmail.com");
        user.setRole(UserRole.CLIENT);
        user.setPassword("123456");

        return authentication(
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                )
        );
    }

    private RequestPostProcessor authenticatedUserId() {

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@gmail.com");
        user.setRole(UserRole.CLIENT);
        user.setPassword("123456");

        return authentication(
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                )
        );
    }
}
