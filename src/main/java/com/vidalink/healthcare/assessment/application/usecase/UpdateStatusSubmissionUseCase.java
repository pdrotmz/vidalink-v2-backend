package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.model.Submission;

import java.util.UUID;

public interface UpdateStatusSubmissionUseCase {

    void approved(UUID id);
    void rejected(UUID id);
}
