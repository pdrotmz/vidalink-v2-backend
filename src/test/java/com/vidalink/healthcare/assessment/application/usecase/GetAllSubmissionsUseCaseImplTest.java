package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllSubmissionsUseCaseImplTest {

    @Mock
    private SubmissionRepository repository;

    @InjectMocks
    private GetAllSubmissionsUseCaseImpl useCase;

    @Test
    void shouldReturnAllSubmissions() {

        Submission firstSubmission = new Submission();
        firstSubmission.setId(UUID.randomUUID());

        Submission secondSubmission = new Submission();
        secondSubmission.setId(UUID.randomUUID());

        when(repository.findAll())
                .thenReturn(List.of(firstSubmission, secondSubmission));

        List<SubmissionResponse> responses = useCase.execute();

        assertEquals(2, responses.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoSubmissions() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<SubmissionResponse> responses = useCase.execute();

        assertTrue(responses.isEmpty());

        verify(repository).findAll();
    }
}
