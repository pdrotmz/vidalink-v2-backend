package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;

import java.util.UUID;

public interface GetSubmissionByIdUseCase {

    SubmissionResponse execute(UUID id);
}
