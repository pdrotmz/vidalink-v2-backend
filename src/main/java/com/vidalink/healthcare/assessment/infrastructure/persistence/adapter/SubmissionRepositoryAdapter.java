package com.vidalink.healthcare.assessment.infrastructure.persistence.adapter;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.assessment.infrastructure.persistence.jpa.JpaSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryAdapter implements SubmissionRepository {

    private final JpaSubmissionRepository jpaSubmissionRepository;

    @Override
    public Submission save(Submission submission) {
        return jpaSubmissionRepository.save(submission);
    }

    @Override
    public Optional<Submission> findById(UUID id) {
        return jpaSubmissionRepository.findById(id);
    }

    @Override
    public List<Submission> findAll() {
        return jpaSubmissionRepository.findAll();
    }

    @Override
    public List<Submission> findByUserId(UUID id) {
        return jpaSubmissionRepository.findSubmissionsByIdUser(id);
    }

    @Override
    public List<SubmissionResponse> findByStatus(ValidationStatus status) {
        return jpaSubmissionRepository.findByStatus(status);
    }

    @Override
    public void deleteById(UUID id) {
        jpaSubmissionRepository.deleteById(id);
    }
}
