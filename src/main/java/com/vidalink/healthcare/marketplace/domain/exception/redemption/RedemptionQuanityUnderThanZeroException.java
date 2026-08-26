package com.vidalink.healthcare.marketplace.domain.exception.redemption;

public class RedemptionQuanityUnderThanZeroException extends RuntimeException {
    public RedemptionQuanityUnderThanZeroException(String message) {
        super(message);
    }
}
