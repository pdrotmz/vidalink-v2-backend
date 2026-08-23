package com.vidalink.healthcare.marketplace.application.dto.response;

import com.vidalink.healthcare.marketplace.domain.model.Reward;
import org.springframework.web.multipart.MultipartFile;

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
