package com.vidalink.healthcare.assessment.application.dto;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionResponse(
        UUID id,
        UUID idUser,
        LocalDateTime sentTime,
        String filePath,
        ValidationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static SubmissionResponse from(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getIdUser(),
                submission.getSentTime(),
                submission.getFile(),
                submission.getStatus(),
                submission.getCreatedAt(),
                submission.getUpdatedAt()
        );
    }
}
