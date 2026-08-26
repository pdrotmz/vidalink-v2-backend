package com.vidalink.healthcare.assessment.application.usecase;

import java.util.UUID;

public interface UpdateStatusSubmissionUseCase {

    void approved(UUID id);
    void rejected(UUID id);
}
