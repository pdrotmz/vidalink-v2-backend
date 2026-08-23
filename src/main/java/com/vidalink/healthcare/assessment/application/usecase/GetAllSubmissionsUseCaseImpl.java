package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllSubmissionsUseCaseImpl implements GetAllSubmissionsUseCase {

    private final SubmissionRepository repository;

    @Override
    public List<SubmissionResponse> execute() {
        return repository.findAll()
                .stream()
                .map(SubmissionResponse::from)
                .toList();

    }
}
