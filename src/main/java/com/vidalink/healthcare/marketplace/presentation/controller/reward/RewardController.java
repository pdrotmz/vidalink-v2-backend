package com.vidalink.healthcare.marketplace.presentation.controller.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.application.usecase.reward.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rewards")
public class RewardController {

    private final CreateRewardUseCaseImpl createRewardUseCase;
    private final GetAllRewardsUseCaseImpl getAllRewardsUseCase;
    private final GetRewardByIdUseCaseImpl getRewardByIdUseCase;
    private final GetRewardByNameUseCaseImpl getRewardByNameUseCase;
    private final GetAllRewardsByNameIgnoreCaseUseCaseImpl getAllRewardsByNameIgnoreCaseUseCase;
    private final DeactivateRewardUseCaseImpl deactivateRewardUseCase;
    private final UpdateRewardUseCaseImpl updateRewardUseCase;
    private final UpdateRewardImageUseCaseImpl updateRewardImageUseCase;

    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RewardResponse> create(
            @RequestPart("reward") @Valid CreateRewardRequest request,
            @RequestPart("image") MultipartFile image) {
        RewardResponse createdReward = createRewardUseCase.execute(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReward);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RewardResponse>> getAll() {
        List<RewardResponse> rewards = getAllRewardsUseCase.execute();
        return ResponseEntity.ok().body(rewards);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> getById(@PathVariable("id") @Valid UUID id) {
        RewardResponse rewardId = getRewardByIdUseCase.execute(id);
        return ResponseEntity.accepted().body(rewardId);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> getByName(@PathVariable("name") @Valid String name) {
        RewardResponse rewardName = getRewardByNameUseCase.execute(name);
        return ResponseEntity.accepted().body(rewardName);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<RewardResponse>> getAllSearch(@RequestParam(value = "keyword", required = false) String name) {
        List<RewardResponse> rewards = getAllRewardsByNameIgnoreCaseUseCase.execute(name);
        return ResponseEntity.accepted().body(rewards);
    }

    @PatchMapping("/id/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deactivate(@PathVariable @Valid UUID id) {
        deactivateRewardUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> update(@PathVariable UUID id, @RequestBody @Valid UpdateRewardRequest  request) {
        RewardResponse response = updateRewardUseCase.execute(id, request);
        return ResponseEntity.accepted().body(response);
    }

    @PatchMapping(
            value = "/id/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> updateImage(@PathVariable UUID id, @RequestPart("image") MultipartFile image) {
        RewardResponse response = updateRewardImageUseCase.execute(id, image);

        return ResponseEntity.accepted().body(response);
    }
}
