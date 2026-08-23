package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

import java.util.UUID;

public interface GetRewardByIdUseCase {

    RewardResponse execute(UUID id);
}
