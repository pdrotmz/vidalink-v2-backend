package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UpdateRewardImageUseCase {

    RewardResponse execute(UUID rewardId, MultipartFile image);
}
