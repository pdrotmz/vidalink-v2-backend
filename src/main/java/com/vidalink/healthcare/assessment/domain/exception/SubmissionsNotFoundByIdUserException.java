package com.vidalink.healthcare.assessment.domain.exception;

import java.util.UUID;

public class SubmissionsNotFoundByIdUserException extends RuntimeException {
    public SubmissionsNotFoundByIdUserException(UUID id) {
        super(
                "Submission not found with id user: " + id
        );
    }
}
