package com.vidalink.healthcare.marketplace.domain.exception.redemption;

public class RedemptionNotFoundByIdUserException extends RuntimeException {
    public RedemptionNotFoundByIdUserException(String message) {
        super(message);
    }
}
