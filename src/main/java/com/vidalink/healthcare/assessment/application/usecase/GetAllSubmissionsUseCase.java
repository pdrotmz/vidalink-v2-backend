package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;

import java.util.List;

public interface GetAllSubmissionsUseCase {
    List<SubmissionResponse> execute();
}
