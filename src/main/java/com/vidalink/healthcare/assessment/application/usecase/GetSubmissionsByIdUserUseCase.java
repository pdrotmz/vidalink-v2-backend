package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.model.Submission;

import java.util.List;
import java.util.UUID;

public interface GetSubmissionsByIdUserUseCase {

    List<Submission> execute(UUID id);
}
