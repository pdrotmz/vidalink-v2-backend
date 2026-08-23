package com.vidalink.healthcare.assessment.infrastructure.persistence.jpa;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaSubmissionRepository extends JpaRepository<Submission, UUID> {

    @Override
    Optional<Submission> findById(UUID id);

    List<Submission> findSubmissionsByIdUser(UUID idUser);

    List<SubmissionResponse> findByStatus(ValidationStatus status);

    void deleteById(UUID id);
}
