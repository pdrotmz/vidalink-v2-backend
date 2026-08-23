package com.vidalink.healthcare.marketplace.domain.exception.redemption;

public class RedemptionAmountUnderThanZeroException extends RuntimeException {
    public RedemptionAmountUnderThanZeroException(String message) {
        super(message);
    }
}
