package com.vidalink.healthcare.marketplace.presentation.controller.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardImageResponse;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.application.usecase.reward.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.UUID;

@Tag(
        name = "Marketplace - Reward",
        description = "Endpoints to reward management"
)
@SecurityRequirement(name = "bearerAuth")
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
    private final GetRewardImageUseCaseImpl getRewardImageUseCase;

    @Operation(summary = "Register a reward", description = "Creates a reward")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Register a reward"),
            @ApiResponse(responseCode = "400", description = "Redemption has a bad request")
    })
    @PostMapping(
            value = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RewardResponse> create(
            @RequestPart("reward") @Valid CreateRewardRequest request,
            @RequestPart("image") MultipartFile image) {
        RewardResponse createdReward = createRewardUseCase.execute(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReward);
    }

    @Operation(summary = "Get all rewards", description = "Admin get all rewards")
    @ApiResponse(responseCode = "200", description = "Return all rewards")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RewardResponse>> getAll() {
        List<RewardResponse> rewards = getAllRewardsUseCase.execute();
        return ResponseEntity.ok().body(rewards);
    }

    @Operation(summary = "Get reward by ID", description = "Get a specific reward info by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reward info successfully returned by ID"),
            @ApiResponse(responseCode = "404", description = "Reward with this ID was not found")
    })
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> getById(
            @Parameter(description = "Unique identifier of the reward.", required = true)
            @PathVariable("id") @Valid UUID id) {
        RewardResponse rewardId = getRewardByIdUseCase.execute(id);
        return ResponseEntity.accepted().body(rewardId);
    }

    @GetMapping("/id/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable UUID id) {

        RewardImageResponse image = getRewardImageUseCase.execute(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new InputStreamResource(image.inputStream()));
    }

    @Operation(summary = "Get reward by name", description = "Get a specific reward info by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "reward info successfully returned by name"),
            @ApiResponse(responseCode = "404", description = "reward with this name was not found")
    })
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> getByName(
            @Parameter(description = "Identifier name of reward", required = true)
            @PathVariable("name") @Valid String name) {
        RewardResponse rewardName = getRewardByNameUseCase.execute(name);
        return ResponseEntity.accepted().body(rewardName);
    }

    @Operation(summary = "Get reward by name ignore case", description = "Gets a list of rewards by ignore case")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reward info successfully returned by ID"),
            @ApiResponse(responseCode = "404", description = "reward with this ID was not found")
    })
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<RewardResponse>> getAllSearch(
            @Parameter(description = "Type anything to search", required = false)
            @RequestParam(value = "keyword", required = false) String name) {
        List<RewardResponse> rewards = getAllRewardsByNameIgnoreCaseUseCase.execute(name);
        return ResponseEntity.accepted().body(rewards);
    }

    @Operation(summary = "Delete reward by ID", description = "Get a specific reward info by ID and delete it")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reward successfully deleted by ID"),
            @ApiResponse(responseCode = "404", description = "Reward with this ID was not found")
    })
    @PatchMapping("/id/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "Unique identifier of the reward.", required = true)
            @PathVariable @Valid UUID id) {
        deactivateRewardUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update reward by ID", description = "Get a specific reward by ID and update its info")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reward info successfully updated by ID"),
            @ApiResponse(responseCode = "404", description = "Reward with this ID was not found")
    })
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> update(
            @Parameter(description = "Unique identifier of the reward.", required = true)
            @PathVariable UUID id, @RequestBody @Valid UpdateRewardRequest  request) {
        RewardResponse response = updateRewardUseCase.execute(id, request);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Update reward image by ID", description = "Get a specific reward by ID and upload reward image")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Reward image successfully updated by ID"),
            @ApiResponse(responseCode = "404", description = "Reward with this ID was not found")
    })
    @PatchMapping(value = "/id/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RewardResponse> updateImage(
            @Parameter(description = "Unique identifier of the reward.", required = true)
            @PathVariable UUID id, @RequestPart("image") MultipartFile image) {
        RewardResponse response = updateRewardImageUseCase.execute(id, image);

        return ResponseEntity.accepted().body(response);
    }
}
