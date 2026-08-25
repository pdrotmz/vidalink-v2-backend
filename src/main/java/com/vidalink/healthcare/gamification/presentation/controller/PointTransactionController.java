package com.vidalink.healthcare.gamification.presentation.controller;

import com.vidalink.healthcare.gamification.application.dto.request.pointtransaction.RegisterPointTransactionRequest;
import com.vidalink.healthcare.gamification.application.dto.request.userbadge.AwardBadgeRequest;
import com.vidalink.healthcare.gamification.application.dto.response.level.UserLevelResponse;
import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.application.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.application.dto.response.userbadge.UserBadgeResponse;
import com.vidalink.healthcare.gamification.application.usecase.level.GetUserLevelUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.points.GetUserPointsUseCase;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.GetUserPointTransactionsUseCase;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCase;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.AwardBadgeUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.GetUserBadgesUseCaseImpl;
import com.vidalink.healthcare.identity.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointTransactionController {

    private final RegisterPointTransactionUseCase registerPointTransactionUseCase;
    private final GetUserPointsUseCase getUserPointsUseCase;
    private final GetUserPointTransactionsUseCase getUserPointTransactionsUseCase;
    private final GetUserLevelUseCaseImpl getUserLevelUseCase;
    private final AwardBadgeUseCaseImpl awardBadgeUseCase;
    private final GetUserBadgesUseCaseImpl getUserBadgesUseCase;

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerPointTransaction(@RequestBody RegisterPointTransactionRequest request) {
        registerPointTransactionUseCase.execute(
                request.userId(),
                request.amount(),
                request.type(),
                request.source()
        );
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserPointsResponse getUserPoints(@AuthenticationPrincipal User user) {
        return getUserPointsUseCase.execute(user.getId());
    }

    @GetMapping("/me/transactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PointTransactionResponse> getUserPointTransactions(@AuthenticationPrincipal User user) {
        return getUserPointTransactionsUseCase.execute(user.getId());
    }
    @GetMapping("/me/level")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserLevelResponse getUserLevel(@AuthenticationPrincipal User user) {
        return getUserLevelUseCase.execute(user.getId());
    }
    @PostMapping("/{userId}/badges")
    @ResponseStatus(HttpStatus.CREATED)
    public void awardBadge(@PathVariable UUID userId, @Valid @RequestBody AwardBadgeRequest request) {
        awardBadgeUseCase.execute(userId, request.badge());
    }

    @GetMapping("/me/badges")
    @ResponseStatus(HttpStatus.OK)
    public List<UserBadgeResponse> getUserBadges(@AuthenticationPrincipal User user) {
        return getUserBadgesUseCase.execute(user.getId());
    }

}
