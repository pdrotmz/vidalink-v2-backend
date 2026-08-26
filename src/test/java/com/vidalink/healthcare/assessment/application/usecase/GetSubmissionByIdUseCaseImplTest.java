package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSubmissionByIdUseCaseImplTest {

    @Mock
    private SubmissionRepository repository;

    @InjectMocks
    private GetSubmissionByIdUseCaseImpl useCase;

    @Test
    void shouldReturnSubmissionById() {

        UUID id = UUID.randomUUID();

        Submission submission = new Submission();
        submission.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(submission));

        SubmissionResponse response = useCase.execute(id);

        assertNotNull(response);
        assertEquals(id, response.id());

        verify(repository).findById(id);
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
    }
}
