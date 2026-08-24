package com.vidalink.healthcare.assessment.presentation.controller;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.application.usecase.*;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
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

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SubmissionResponse> send(Authentication authentication, @RequestPart("file") MultipartFile file) {

        String email = authentication.getName();

        SubmissionResponse response = sentSubmissionUseCase.execute(email, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SubmissionResponse>> getAll() {
        List<SubmissionResponse> responses = getAllSubmissionsUseCase.execute();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<SubmissionResponse> getById(@PathVariable UUID id) {
        SubmissionResponse response = getSubmissionByIdUseCase.execute(id);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/id/user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<Submission>> GetByIdUser(@PathVariable UUID id) {
        List<Submission> responses = getSubmissionsByIdUserUseCase.execute(id);
        return ResponseEntity.accepted().body(responses);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SubmissionResponse>> getByStatus(@Valid ValidationStatus status) {
        List<SubmissionResponse> responses = getSubmissionsByStatusUseCase.execute(status);
        return ResponseEntity.accepted().body(responses);
    }

    @PatchMapping("/id/status/{id}/approve")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> approveStatus(@PathVariable UUID id) {
        updateStatusSubmissionUseCase.approved(id);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/id/status/{id}/reject")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> rejectStatus(@PathVariable UUID id) {
        updateStatusSubmissionUseCase.rejected(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/id/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        deleteSubmissionByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
