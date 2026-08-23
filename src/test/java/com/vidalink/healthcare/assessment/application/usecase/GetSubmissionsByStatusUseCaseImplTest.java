package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSubmissionsByStatusUseCaseImplTest {

    @Mock
    private SubmissionRepository repository;

    @InjectMocks
    private GetSubmissionsByStatusUseCaseImpl useCase;

    @Test
    void shouldReturnSubmissionsByStatus() {

        SubmissionResponse response = mock(SubmissionResponse.class);

        when(repository.findByStatus(ValidationStatus.PENDING))
                .thenReturn(List.of(response));

        List<SubmissionResponse> responses =
                useCase.execute(ValidationStatus.PENDING);

        assertEquals(1, responses.size());

        verify(repository)
                .findByStatus(ValidationStatus.PENDING);
    }

    @Test
    void shouldReturnEmptyListWhenNoSubmissionHasStatus() {

        when(repository.findByStatus(ValidationStatus.APPROVED))
                .thenReturn(List.of());

        List<SubmissionResponse> responses =
                useCase.execute(ValidationStatus.APPROVED);

        assertTrue(responses.isEmpty());

        verify(repository)
                .findByStatus(ValidationStatus.APPROVED);
    }
}
