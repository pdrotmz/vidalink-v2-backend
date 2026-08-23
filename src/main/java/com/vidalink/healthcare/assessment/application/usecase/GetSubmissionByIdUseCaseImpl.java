package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetSubmissionByIdUseCaseImpl implements GetSubmissionByIdUseCase{

    private final SubmissionRepository repository;


    @Override
    public SubmissionResponse execute(UUID id) {

        Submission submission = repository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundByIdException(id));

        return SubmissionResponse.from(submission);
    }
}
