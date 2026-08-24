package com.vidalink.healthcare.marketplace.presentation.controller;

import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.infrastructure.persistence.jwt.JwtService;
import com.vidalink.healthcare.shared.infrastructure.security.UserDetailsServiceImpl;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.application.usecase.redemption.*;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.*;
import com.vidalink.healthcare.marketplace.presentation.controller.redemption.RedemptionController;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedemptionController.class)
@AutoConfigureMockMvc(addFilters = false)
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

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
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

        when(createRedemptionUseCase.execute(any(CreateRedemptionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/redemptions/redeem")
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
                .andExpect(jsonPath("$.amount")
                        .value(2));

        verify(createRedemptionUseCase)
                .execute(any(CreateRedemptionRequest.class));
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

        mockMvc.perform(get("/api/redemptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(getAllRedemptionsUseCase).execute();
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
                )
                .andExpect(status().isOk())
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
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReward")
                        .value(rewardId.toString()));

        verify(getRedemptionByIdRewardUseCase)
                .execute(rewardId);
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByUserIsNotFound() throws Exception {

        UUID idUser = UUID.randomUUID();

        when(getRedemptionByIdUserUseCase.execute(idUser))
                .thenThrow(new RedemptionNotFoundByIdUserException(
                        "Redemption not found with id user: " + idUser
                ));

        mockMvc.perform(get("/api/redemptions/user/id/{id}", idUser))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Redemption not found with id user: " + idUser));
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByRewardIsNotFound() throws Exception {

        UUID idReward = UUID.randomUUID();

        when(getRedemptionByIdRewardUseCase.execute(idReward))
                .thenThrow(new RedemptionNotFoundByIdRewardException(
                        "Redemption not found with id reward: " + idReward
                ));

        mockMvc.perform(get("/api/redemptions/reward/id/{id}", idReward))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldReturnNotFoundWhenRedemptionByIdIsNotFound() throws Exception {

        UUID redemptionId = UUID.randomUUID();

        when(getRedemptionByIdUseCase.execute(redemptionId))
                .thenThrow(new RedemptionNotFoundByIdException(
                        "Redemption not found with id: " + redemptionId
                ));

        mockMvc.perform(get("/api/redemptions/id/{id}", redemptionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldReturnBadRequestWhenRewardHasInsufficientStock() throws Exception {

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        10
                );

        when(createRedemptionUseCase.execute(any(CreateRedemptionRequest.class)))
                .thenThrow(new RewardInsufficientStockException(
                        "Insufficient reward stock"
                ));

        mockMvc.perform(post("/api/redemptions/redeem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Insufficient reward stock"));
    }

    @Test
    void shouldReturnBadRequestWhenRedemptionAmountIsInvalid() throws Exception {

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        0
                );

        when(createRedemptionUseCase.execute(any(CreateRedemptionRequest.class)))
                .thenThrow(new RedemptionAmountUnderThanZeroException(
                        "Redemption amount must be greater than zero"
                ));

        mockMvc.perform(post("/api/redemptions/redeem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        UUID idUser = UUID.randomUUID();

        when(getRedemptionByIdUserUseCase.execute(idUser))
                .thenThrow(new UserNotFoundException(
                        "User not found with id: " + idUser
                ));

        mockMvc.perform(get("/api/redemptions/user/id/{id}", idUser))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
