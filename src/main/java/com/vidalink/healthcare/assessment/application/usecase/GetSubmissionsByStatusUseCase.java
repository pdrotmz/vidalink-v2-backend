package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;

import java.util.List;

public interface GetSubmissionsByStatusUseCase {

    List<SubmissionResponse> execute(ValidationStatus status);
}
