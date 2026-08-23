package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSubmissionByIdUseCaseImplTest {

    @Mock
    private SubmissionRepository repository;

    @InjectMocks
    private DeleteSubmissionByIdUseCaseImpl useCase;

    @Test
    void shouldDeleteSubmissionSuccessfully() {

        UUID id = UUID.randomUUID();

        Submission submission = new Submission();
        submission.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(submission));

        useCase.execute(id);

        verify(repository).findById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenSubmissionDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmissionNotFoundByIdException.class,
                () -> useCase.execute(id)
        );

        verify(repository).findById(id);
        verify(repository, never()).deleteById(any());
    }
}
