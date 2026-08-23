package com.vidalink.healthcare.marketplace.presentation.controller.redemption;

import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.application.usecase.redemption.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/redemptions")
@RequiredArgsConstructor
public class RedemptionController {

    private final CreateRedemptionUseCaseImpl createRedemptionUseCase;
    private final GetAllRedemptionsUseCaseImpl getAllRedemptionsUseCase;
    private final GetRedemptionByIdUseCaseImpl getRedemptionByIdUseCase;
    private final GetRedemptionByIdUserUseCaseImpl getRedemptionByIdUserUseCase;
    private final GetRedemptionByIdRewardUseCaseImpl getRedemptionByIdRewardUseCase;

    @PostMapping("/redeem")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> redeem(@RequestBody @Valid CreateRedemptionRequest request) {
        RedemptionResponse response = createRedemptionUseCase.execute(request);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RedemptionResponse>> getAll() {
        List<RedemptionResponse> responses = getAllRedemptionsUseCase.execute();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getById(@PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdUseCase.execute(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getByIdUser(@PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdUserUseCase.execute(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/reward/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RedemptionResponse> getByIdReward(@PathVariable UUID id) {
        RedemptionResponse response = getRedemptionByIdRewardUseCase.execute(id);
        return ResponseEntity.ok().body(response);
    }
}
