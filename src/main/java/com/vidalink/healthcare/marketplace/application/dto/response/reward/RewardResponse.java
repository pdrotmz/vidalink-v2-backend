package com.vidalink.healthcare.marketplace.application.dto.response.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;

import java.util.UUID;

public record RewardResponse(
        UUID id,
        String name,
        String description,
        int stock,
        String image,
        boolean isActive
) {

    public static RewardResponse from(Reward reward) {
        return new RewardResponse(
                reward.getId(),
                reward.getName(),
                reward.getDescription(),
                reward.getStock(),
                reward.getImagePath(),
                reward.isActive()
        );
    }
}
