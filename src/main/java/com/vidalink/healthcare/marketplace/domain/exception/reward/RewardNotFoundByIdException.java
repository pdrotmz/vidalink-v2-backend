package com.vidalink.healthcare.marketplace.domain.exception.reward;

import java.util.UUID;

public class RewardNotFoundByIdException extends RuntimeException {
    public RewardNotFoundByIdException(UUID id) {
        super(
                "Reward not found with id" + id
        );
    }
}
