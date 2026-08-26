package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCase;
import com.vidalink.healthcare.gamification.application.usecase.userbadge.AwardBadgeUseCaseImpl;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.identity.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateStatusSubmissionUseCaseImpl implements UpdateStatusSubmissionUseCase {

    private static final Integer SUBMISSION_APPROVED_POINTS = 100;

    private final SubmissionRepository submissionRepository;
    private final RegisterPointTransactionUseCase registerPointTransactionUseCase;
    private final AwardBadgeUseCaseImpl awardBadgeUseCase;
    private final UserBadgeRepository userBadgeRepository;

    @Transactional
    @Override
    public void approved(UUID id) {

        User user = new User();

        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() ->
                        new SubmissionNotFoundByIdException(id)
                );

        submission.setStatus(ValidationStatus.APPROVED);

        registerPointTransactionUseCase.execute(
                submission.getIdUser(),
                SUBMISSION_APPROVED_POINTS,
                PointTransactionType.CREDIT,
                PointTransactionSource.ASSESSMENT
        );
        awardBadgeUseCase.execute(submission.getIdUser(), Badge.FIRST_CONTRIBUTION);

        submissionRepository.save(submission);
    }

    @Transactional
    @Override
    public void rejected(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new SubmissionNotFoundByIdException(id));

        submission.setStatus(ValidationStatus.REJECTED);
    }
}
