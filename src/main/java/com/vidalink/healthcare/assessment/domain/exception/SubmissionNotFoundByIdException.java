package com.vidalink.healthcare.assessment.domain.exception;

import java.util.UUID;

public class SubmissionNotFoundByIdException extends RuntimeException {
    public SubmissionNotFoundByIdException(UUID id) {
        super(
                "Submission not found with id: " + id
        );
    }
}
