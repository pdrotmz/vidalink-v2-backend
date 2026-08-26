package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.AwardBadgeUseCaseImpl;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
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

    private static final Integer SUBMISSION_APPROVED_POINTS = 100;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private RegisterPointTransactionUseCaseImpl registerPointTransactionUseCase;

    @Mock
    private AwardBadgeUseCaseImpl awardBadgeUseCase;

    @InjectMocks
    private UpdateStatusSubmissionUseCaseImpl useCase;

    @Test
    void shouldApproveSubmissionSuccessfully() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Submission submission = new Submission();
        submission.setId(id);
        submission.setIdUser(userId);
        submission.setStatus(ValidationStatus.PENDING);

        when(submissionRepository.findById(id))
                .thenReturn(Optional.of(submission));

        useCase.approved(id);

        assertEquals(
                ValidationStatus.APPROVED,
                submission.getStatus()
        );

        verify(submissionRepository).findById(id);

        verify(submissionRepository).save(submission);

        verify(registerPointTransactionUseCase).execute(
                userId,
                SUBMISSION_APPROVED_POINTS,
                PointTransactionType.CREDIT,
                PointTransactionSource.ASSESSMENT
        );

        verify(awardBadgeUseCase).execute(
                userId,
                Badge.FIRST_CONTRIBUTION
        );
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

