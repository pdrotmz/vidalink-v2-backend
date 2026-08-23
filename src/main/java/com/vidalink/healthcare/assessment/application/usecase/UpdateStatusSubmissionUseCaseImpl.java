package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateStatusSubmissionUseCaseImpl implements UpdateStatusSubmissionUseCase {

    private final SubmissionRepository submissionRepository;


    @Transactional
    @Override
    public void approved(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundByIdException(id));

        submission.setStatus(ValidationStatus.APPROVED);
    }

    @Transactional
    @Override
    public void rejected(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundByIdException(id));

        submission.setStatus(ValidationStatus.REJECTED);
    }
}
