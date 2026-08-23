package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.request.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

public interface CreateRewardUseCase {

    RewardResponse execute(CreateRewardRequest request);
}
