package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteSubmissionByIdUseCaseImpl implements DeleteSubmissionByIdUseCase{

    private final SubmissionRepository repository;


    @Override
    public void execute(UUID id) {
        repository.findById(id).orElseThrow(() -> new SubmissionNotFoundByIdException(id));
        repository.deleteById(id);
    }
}
