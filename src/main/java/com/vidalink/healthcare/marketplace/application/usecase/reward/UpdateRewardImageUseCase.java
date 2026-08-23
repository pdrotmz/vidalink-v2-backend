package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UpdateRewardImageUseCase {

    RewardResponse execute(UUID rewardId, MultipartFile image);
}
