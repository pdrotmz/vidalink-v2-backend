package com.vidalink.healthcare.marketplace.domain.exception.redemption;

public class RedemptionNotFoundByIdException extends RuntimeException {
    public RedemptionNotFoundByIdException(String message) {
        super(message);
    }
}
