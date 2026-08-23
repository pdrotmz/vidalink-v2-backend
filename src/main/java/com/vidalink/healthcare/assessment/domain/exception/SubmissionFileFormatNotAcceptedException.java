package com.vidalink.healthcare.assessment.domain.exception;

public class SubmissionFileFormatNotAcceptedException extends RuntimeException {
    public SubmissionFileFormatNotAcceptedException(String message) {
        super(message);
    }
}
