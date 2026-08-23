package com.vidalink.healthcare.marketplace.domain.exception.redemption;

public class RewardInsufficientStockException extends RuntimeException {
    public RewardInsufficientStockException(String message) {
        super(message);
    }
}
