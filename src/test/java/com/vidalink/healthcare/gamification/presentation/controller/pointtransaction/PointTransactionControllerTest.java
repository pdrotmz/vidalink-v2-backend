package com.vidalink.healthcare.gamification.presentation.controller.pointtransaction;

import com.vidalink.healthcare.gamification.application.dto.request.pointtransaction.RegisterPointTransactionRequest;
import com.vidalink.healthcare.gamification.application.dto.request.userbadge.AwardBadgeRequest;
import com.vidalink.healthcare.gamification.application.dto.response.level.UserLevelResponse;
import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.application.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.application.dto.response.userbadge.UserBadgeResponse;
import com.vidalink.healthcare.gamification.application.usecase.level.GetUserLevelUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.points.GetUserPointsUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.GetUserPointTransactionsUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.AwardBadgeUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.GetUserBadgesUseCaseImpl;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.enums.level.Level;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.presentation.controller.PointTransactionController;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointTransactionController.class)
class PointTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private RegisterPointTransactionUseCaseImpl registerPointTransactionUseCase;

    @MockitoBean
    private GetUserPointsUseCaseImpl getUserPointsUseCase;

    @MockitoBean
    private GetUserPointTransactionsUseCaseImpl getUserPointTransactionsUseCase;

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

        mockMvc.perform(
                        post("/api/points/transactions")
                                .with(authenticatedAdmin(UUID.randomUUID()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
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
                new UserPointsResponse(
                        userId,
                        100
                );

        when(getUserPointsUseCase.execute(userId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/points/me")
                                .with(authenticatedUser(userId))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.userId")
                        .value(userId.toString()))
                .andExpect(jsonPath("$.balance")
                        .value(100));

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

        mockMvc.perform(
                        get("/api/points/me/transactions")
                                .with(authenticatedUser(userId))
                )
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

        mockMvc.perform(
                        get("/api/points/me/level")
                                .with(authenticatedUser(userId))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.userId")
                        .value(userId.toString()))
                .andExpect(jsonPath("$.level")
                        .value("ADVANCED"))
                .andExpect(jsonPath("$.totalPoints")
                        .value(1500));

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
                                .with(authenticatedAdmin(userId))
                                .with(csrf())
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
                .thenReturn(List.of(
                        firstBadge,
                        secondBadge
                ));

        mockMvc.perform(
                        get("/api/points/me/badges")
                                .with(authenticatedAdmin(userId))
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

    private RequestPostProcessor authenticatedUser(UUID userId) {

        User user = new User();
        user.setId(userId);
        user.setEmail("user@test.com");
        user.setPassword("123456");

        return authentication(
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                )
        );
    }

    private RequestPostProcessor authenticatedAdmin(UUID userId) {

        User admin = new User();
        admin.setId(userId);
        admin.setEmail("admin@vidalink.com");
        admin.setPassword("123456");

        return authentication(
                new UsernamePasswordAuthenticationToken(
                        admin,
                        null,
                        admin.getAuthorities()
                )
        );
    }
}
