package com.vidalink.healthcare.assessment.domain.exception;

public class SubmissionExceedFileSizeException extends RuntimeException {
    public SubmissionExceedFileSizeException(String message) {
        super(message);
    }
}
