package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

import java.util.List;
import java.util.UUID;

public interface GetSubmissionsByIdUserUseCase {

    List<Submission> execute(UUID id);
}
