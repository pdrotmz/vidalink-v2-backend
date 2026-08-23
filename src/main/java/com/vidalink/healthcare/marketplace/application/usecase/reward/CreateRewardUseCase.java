package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;

public interface CreateRewardUseCase {

    RewardResponse execute(CreateRewardRequest request);
}
