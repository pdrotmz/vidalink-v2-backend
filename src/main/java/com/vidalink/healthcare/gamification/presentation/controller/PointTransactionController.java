package com.vidalink.healthcare.gamification.presentation.controller;

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
import com.vidalink.healthcare.identity.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Gamification - Point Transactions",
        description = "Endpoints to view level, badges and points"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointTransactionController {

    private final RegisterPointTransactionUseCaseImpl registerPointTransactionUseCase;
    private final GetUserPointsUseCaseImpl getUserPointsUseCase;
    private final GetUserPointTransactionsUseCaseImpl getUserPointTransactionsUseCase;
    private final GetUserLevelUseCaseImpl getUserLevelUseCase;
    private final AwardBadgeUseCaseImpl awardBadgeUseCase;
    private final GetUserBadgesUseCaseImpl getUserBadgesUseCase;

    @Operation(summary = "Register a transaction", description = "Register a transaction for an user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction registered."),
            @ApiResponse(responseCode = "400", description = "Response has a bad request, transaction not registered.")
    })
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

    @Operation(summary = "Get user points", description = "Return user's points based on their login.")
    @ApiResponses({
            @ApiResponse(responseCode = "202",description = "User points successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
})
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserPointsResponse getUserPoints(@AuthenticationPrincipal User user) {
        return getUserPointsUseCase.execute(user.getId());
    }

    @Operation(summary = "Get all user transactions", description = "Return all transactions user did.")
    @ApiResponses({
            @ApiResponse(responseCode = "202",description = "User transactions successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
    })
    @GetMapping("/me/transactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<PointTransactionResponse> getUserPointTransactions(@AuthenticationPrincipal User user) {
        return getUserPointTransactionsUseCase.execute(user.getId());
    }

    @Operation(summary = "Get user level", description = "Return user level based on his approved submissions.")
    @ApiResponses({
            @ApiResponse(responseCode = "202",description = "User level successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
    })
    @GetMapping("/me/level")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserLevelResponse getUserLevel(@AuthenticationPrincipal User user) {
        return getUserLevelUseCase.execute(user.getId());
    }

    @Operation(summary = "Get badge by specific user ID", description = "Get a specific badge info by user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202",description = "Badge info successfully returned by user ID."),
            @ApiResponse(responseCode = "404", description = "User ID was not found.")
    })
    @PostMapping("/{userId}/badges")
    @ResponseStatus(HttpStatus.CREATED)
    public void awardBadge(
            @Parameter(description = "Unique identifier of the user.", required = true)
            @PathVariable UUID userId, @Valid @RequestBody AwardBadgeRequest request) {
        awardBadgeUseCase.execute(userId, request.badge());
    }

    @Operation(summary = "Get user badges", description = "Return user's badges based on their login.")
    @ApiResponses({
            @ApiResponse(responseCode = "202",description = "User badge successfully returned."),
            @ApiResponse(responseCode = "401", description = "Unauthenticated user.")
    })
    @GetMapping("/me/badges")
    @ResponseStatus(HttpStatus.OK)
    public List<UserBadgeResponse> getUserBadges(@AuthenticationPrincipal User user) {
        return getUserBadgesUseCase.execute(user.getId());
    }

}
