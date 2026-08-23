package com.vidalink.healthcare.assessment.domain.exception;

public class SubmissionNotSentException extends RuntimeException {
    public SubmissionNotSentException(String message) {
        super(message);
    }
}
