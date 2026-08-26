package com.vidalink.healthcare.assessment.presentation.controller;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.application.usecase.*;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Assessment - Submissions",
        description = "Endpoints for submission management"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SendSubmissionUseCaseImpl sentSubmissionUseCase;
    private final GetAllSubmissionsUseCaseImpl getAllSubmissionsUseCase;
    private final GetSubmissionByIdUseCaseImpl getSubmissionByIdUseCase;
    private final GetSubmissionsByIdUserUseCaseImpl getSubmissionsByIdUserUseCase;
    private final GetSubmissionsByStatusUseCaseImpl getSubmissionsByStatusUseCase;
    private final UpdateStatusSubmissionUseCaseImpl updateStatusSubmissionUseCase;
    private final DeleteSubmissionByIdUseCaseImpl deleteSubmissionByIdUseCase;

    @Operation(summary = "Send a submission", description = "Send a new submission for admin analysis")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Submission sent"),
            @ApiResponse(responseCode = "400", description = "Submission not sent")
    })
    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubmissionResponse> send(Authentication authentication, @RequestPart("file") MultipartFile file) {

        String email = authentication.getName();

        SubmissionResponse response = sentSubmissionUseCase.execute(email, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all submissions", description = "Admin get all submissions")
    @ApiResponse(responseCode = "200", description = "Returns all submissions")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SubmissionResponse>> getAll() {
        List<SubmissionResponse> responses = getAllSubmissionsUseCase.execute();
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "Get submission by ID", description = "Get a specific submission info by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Submission info successfully returned by ID"),
            @ApiResponse(responseCode = "404", description = "Submission with this ID was not found")
    })
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<SubmissionResponse> getById(
            @Parameter(description = "Unique identifier of the submission.", required = true)
            @PathVariable UUID id) {
        SubmissionResponse response = getSubmissionByIdUseCase.execute(id);
        return ResponseEntity.accepted().body(response);
    }

    @Operation(summary = "Get submission by user ID", description = "Get a specific submission info by user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Submission info successfully returned by user ID"),
            @ApiResponse(responseCode = "404", description = "Submission with this user ID was not found")
    })
    @GetMapping("/id/user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<Submission>> GetByIdUser(
            @Parameter(description = "Unique identifier of the user ID", required = true)
            @PathVariable UUID id) {
        List<Submission> responses = getSubmissionsByIdUserUseCase.execute(id);
        return ResponseEntity.accepted().body(responses);
    }

    @Operation(summary = "Get submission by status", description = "List all submissions by its status")
    @ApiResponse(responseCode = "200", description = "Submission info successfully returned by its status")
    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SubmissionResponse>> getByStatus(
            @Parameter(description = "submission status", required = true)
            @Valid ValidationStatus status) {
        List<SubmissionResponse> responses = getSubmissionsByStatusUseCase.execute(status);
        return ResponseEntity.accepted().body(responses);
    }

    @Operation(summary = "Approve submission", description = "Update status submission by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Submission successfully approved"),
            @ApiResponse(responseCode = "404", description = "Submission with this ID was not found")
    })
    @PatchMapping("/id/status/{id}/approve")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> approveStatus(
            @Parameter(description = "Unique identifier of the submission ID", required = true)
            @PathVariable UUID id) {
        updateStatusSubmissionUseCase.approved(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Reject submission", description = "Update status submission by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Submission successfully rejected"),
            @ApiResponse(responseCode = "404", description = "Submission with this ID was not found")
    })
    @PatchMapping("/id/status/{id}/reject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> rejectStatus(
            @Parameter(description = "Unique identifier of the submission ID", required = true)
            @PathVariable UUID id) {
        updateStatusSubmissionUseCase.rejected(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete submission", description = "Delete a specific submission by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Submission successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Submission with this ID was not found")
    })
    @DeleteMapping("/id/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Unique identifier of the submission ID", required = true)
            @PathVariable UUID id) {
        deleteSubmissionByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
