package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;

public interface GetRewardByNameUseCase {

    RewardResponse execute(String name);
}
