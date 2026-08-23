package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateStatusSubmissionUseCaseImplTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private UpdateStatusSubmissionUseCaseImpl useCase;

    @Test
    void shouldApproveSubmissionSuccessfully() {

        UUID id = UUID.randomUUID();

        Submission submission = new Submission();
        submission.setId(id);
        submission.setStatus(ValidationStatus.PENDING);

        when(submissionRepository.findById(id))
                .thenReturn(Optional.of(submission));

        useCase.approved(id);

        assertEquals(
                ValidationStatus.APPROVED,
                submission.getStatus()
        );

        verify(submissionRepository).findById(id);
    }

    @Test
    void shouldRejectSubmissionSuccessfully() {

        UUID id = UUID.randomUUID();

        Submission submission = new Submission();
        submission.setId(id);
        submission.setStatus(ValidationStatus.PENDING);

        when(submissionRepository.findById(id))
                .thenReturn(Optional.of(submission));

        useCase.rejected(id);

        assertEquals(
                ValidationStatus.REJECTED,
                submission.getStatus()
        );

        verify(submissionRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonexistentSubmission() {

        UUID id = UUID.randomUUID();

        when(submissionRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmissionNotFoundByIdException.class,
                () -> useCase.approved(id)
        );

        verify(submissionRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenRejectingNonexistentSubmission() {

        UUID id = UUID.randomUUID();

        when(submissionRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmissionNotFoundByIdException.class,
                () -> useCase.rejected(id)
        );

        verify(submissionRepository).findById(id);
    }
}

