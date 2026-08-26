package com.vidalink.healthcare.marketplace.domain.exception.reward;

public class RewardNotFoundByNameException extends RuntimeException {
    public RewardNotFoundByNameException(String name) {

        super(
                "Reward not found with name: " + name
        );
    }
}
