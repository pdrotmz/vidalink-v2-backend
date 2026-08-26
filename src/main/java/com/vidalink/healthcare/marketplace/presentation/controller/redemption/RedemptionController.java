package com.vidalink.healthcare.marketplace.presentation.controller.redemption;

import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.application.usecase.redemption.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Marketplace - Redemption",
        description = "Endpoints to redemptions management"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/redemptions")
@RequiredArgsConstructor
public class RedemptionController {

    private final CreateRedemptionUseCaseImpl createRedemptionUseCase;
    private final GetAllRedemptionsUseCaseImpl getAllRedemptionsUseCase;
    private final GetRedemptionByIdUseCaseImpl getRedemptionByIdUseCase;
    private final GetRedemptionByIdUserUseCaseImpl getRedemptionByIdUserUseCase;
    private final GetRedemptionByIdRewardUseCaseImpl getRedemptionByIdRewardUseCase;

    @Operation(summary = "Register a redemption", description = "Register a redemption for user, if the automatic redemption fails")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register a redemption"),
            @ApiResponse(responseCode = "400", description = "Redemption has a bad request")
    })
    @PostMapping("/redeem")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> redeem(@AuthenticationPrincipal User user, @RequestBody @Valid CreateRedemptionRequest request) {
        RedemptionResponse response = createRedemptionUseCase.execute(user.getId(), request);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Get all redemptions", description = "Admin get all redemptions")
    @ApiResponse(responseCode = "200", description = "Return all redemptions")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RedemptionResponse>> getAll() {
        List<RedemptionResponse> responses = getAllRedemptionsUseCase.execute();
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "Get redemption by ID", description = "Get a specific redemption info by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Redemption info successfully returned by ID"),
            @ApiResponse(responseCode = "404", description = "Redemption with this ID was not found")
    })
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getById(
            @Parameter(description = "Unique identifier of the submission.", required = true)
            @PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdUseCase.execute(id);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Get redemption by user ID", description = "Get a specific redemption info by user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Redemption info successfully returned by user ID"),
            @ApiResponse(responseCode = "404", description = "Redemption with this user ID was not found")
    })
    @GetMapping("/user/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getByIdUser(
            @Parameter(description = "Unique identifier of the user ID.", required = true)
            @PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdUserUseCase.execute(id);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Get redemption by reward ID", description = "Get a specific redemption info by reward ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Redemption info successfully returned by reward ID"),
            @ApiResponse(responseCode = "404", description = "Redemption with this reward ID was not found")
    })
    @GetMapping("/reward/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getByIdReward(
            @Parameter(description = "Unique identifier of the reward ID.", required = true)
            @PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdRewardUseCase.execute(id);
        return ResponseEntity.accepted().body(response);
    }
}
