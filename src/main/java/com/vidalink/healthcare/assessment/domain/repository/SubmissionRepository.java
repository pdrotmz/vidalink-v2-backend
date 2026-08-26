package com.vidalink.healthcare.assessment.domain.repository;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository {

    Submission save(Submission submission);

    Optional<Submission> findById(UUID id);

    List<Submission> findAll();

    List<Submission> findByIdUser(UUID idUser);

    List<SubmissionResponse> findByStatus(ValidationStatus status);

    void deleteById(UUID id);

}
