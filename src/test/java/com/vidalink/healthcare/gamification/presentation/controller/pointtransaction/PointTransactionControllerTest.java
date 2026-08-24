package com.vidalink.healthcare.gamification.presentation.controller.pointtransaction;

import com.vidalink.healthcare.gamification.application.usecase.level.GetUserLevelUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.points.GetUserPointsUseCase;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.GetUserPointTransactionsUseCase;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCase;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.AwardBadgeUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.GetUserBadgesUseCaseImpl;
import com.vidalink.healthcare.gamification.application.dto.request.userbadge.AwardBadgeRequest;
import com.vidalink.healthcare.gamification.application.dto.request.pointtransaction.RegisterPointTransactionRequest;
import com.vidalink.healthcare.gamification.application.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.application.dto.response.userbadge.UserBadgeResponse;
import com.vidalink.healthcare.gamification.application.dto.response.level.UserLevelResponse;
import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.enums.level.Level;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.presentation.controller.PointTransactionController;
import com.vidalink.healthcare.identity.infrastructure.persistence.jwt.JwtService;
import com.vidalink.healthcare.shared.infrastructure.security.UserDetailsServiceImpl;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PointTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private RegisterPointTransactionUseCase registerPointTransactionUseCase;

    @MockitoBean
    private GetUserPointsUseCase getUserPointsUseCase;

    @MockitoBean
    private GetUserPointTransactionsUseCase getUserPointTransactionsUseCase;

    @MockitoBean
    private GetUserLevelUseCaseImpl getUserLevelUseCase;

    @MockitoBean
    private AwardBadgeUseCaseImpl awardBadgeUseCase;

    @MockitoBean
    private GetUserBadgesUseCaseImpl getUserBadgesUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterPointTransaction() throws Exception {
        UUID userId = UUID.randomUUID();

        RegisterPointTransactionRequest request =
                new RegisterPointTransactionRequest(
                        userId,
                        100,
                        PointTransactionType.CREDIT,
                        PointTransactionSource.ASSESSMENT
                );

        mockMvc.perform(post("/api/points/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(registerPointTransactionUseCase).execute(
                userId,
                100,
                PointTransactionType.CREDIT,
                PointTransactionSource.ASSESSMENT
        );
    }

    @Test
    void shouldReturnUserPoints() throws Exception {
        UUID userId = UUID.randomUUID();

        UserPointsResponse response =
                new UserPointsResponse(userId, 100);

        when(getUserPointsUseCase.execute(userId))
                .thenReturn(response);

        mockMvc.perform(get("/api/points/{userId}", userId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.balance").value(100));

        verify(getUserPointsUseCase).execute(userId);
    }

    @Test
    void shouldReturnUserPointTransactions() throws Exception {
        UUID userId = UUID.randomUUID();

        PointTransactionResponse response =
                new PointTransactionResponse(
                        UUID.randomUUID(),
                        userId,
                        100,
                        PointTransactionType.CREDIT,
                        PointTransactionSource.ASSESSMENT,
                        LocalDateTime.now()
                );

        when(getUserPointTransactionsUseCase.execute(userId))
                .thenReturn(List.of(response));

        mockMvc.perform(get(
                        "/api/points/{userId}/transactions",
                        userId
                ))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].userId")
                        .value(userId.toString()))
                .andExpect(jsonPath("$[0].amount")
                        .value(100))
                .andExpect(jsonPath("$[0].type")
                        .value("CREDIT"))
                .andExpect(jsonPath("$[0].source")
                        .value("ASSESSMENT"));

        verify(getUserPointTransactionsUseCase)
                .execute(userId);
    }

    @Test
    void shouldReturnUserLevel() throws Exception {
        UUID userId = UUID.randomUUID();

        UserLevelResponse response =
                new UserLevelResponse(
                        userId,
                        Level.ADVANCED,
                        1500
                );

        when(getUserLevelUseCase.execute(userId))
                .thenReturn(response);

        mockMvc.perform(get("/api/points/{userId}/level", userId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.level").value("ADVANCED"))
                .andExpect(jsonPath("$.totalPoints").value(1500));

        verify(getUserLevelUseCase).execute(userId);
    }

    @Test
    void shouldAwardBadge() throws Exception {
        UUID userId = UUID.randomUUID();

        AwardBadgeRequest request =
                new AwardBadgeRequest(
                        Badge.FIRST_CONTRIBUTION
                );

        mockMvc.perform(
                        post("/api/points/{userId}/badges", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated());

        verify(awardBadgeUseCase).execute(
                userId,
                Badge.FIRST_CONTRIBUTION
        );
    }

    @Test
    void shouldReturnUserBadges() throws Exception {
        UUID userId = UUID.randomUUID();

        UserBadgeResponse firstBadge =
                new UserBadgeResponse(
                        UUID.randomUUID(),
                        userId,
                        Badge.FIRST_CONTRIBUTION,
                        LocalDateTime.now()
                );

        UserBadgeResponse secondBadge =
                new UserBadgeResponse(
                        UUID.randomUUID(),
                        userId,
                        Badge.CONTRIBUTOR,
                        LocalDateTime.now()
                );

        when(getUserBadgesUseCase.execute(userId))
                .thenReturn(List.of(firstBadge, secondBadge));

        mockMvc.perform(
                        get("/api/points/{userId}/badges", userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].badge")
                        .value("FIRST_CONTRIBUTION"))
                .andExpect(jsonPath("$[1].badge")
                        .value("CONTRIBUTOR"));

        verify(getUserBadgesUseCase).execute(userId);
    }
}
