package com.vidalink.healthcare.assessment.application.dto;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionResponse(

        @Schema(description = "Submission ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID idUser,

        @Schema(description = "Submission sent time", example = "2026-08-25T15:30:45.123456789")
        LocalDateTime sentTime,

        @Schema(description = "Submission file path", example = "submissions/123e4567-e89b-12d3-a456-426614174000")
        String filePath,

        @Schema(description = "Submission status", defaultValue = "PENDING", example = "PENDING/APPROVED/REJECTED")
        ValidationStatus status,

        @Schema(description = "When submission was created", example = "2026-08-25T15:30:45.123456789")
        LocalDateTime createdAt,

        @Schema(description = "When submission was updated", example = "2026-08-25T15:50:45.123456789")
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
