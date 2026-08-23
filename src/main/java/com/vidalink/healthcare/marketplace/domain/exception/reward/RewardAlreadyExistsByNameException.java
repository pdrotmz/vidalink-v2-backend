package com.vidalink.healthcare.marketplace.domain.exception.reward;

public class RewardAlreadyExistsByNameException extends RuntimeException {
    public RewardAlreadyExistsByNameException(String message) {
        super(message);
    }
}
