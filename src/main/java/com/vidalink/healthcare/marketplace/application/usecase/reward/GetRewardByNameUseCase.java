package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

public interface GetRewardByNameUseCase {

    RewardResponse execute(String name);
}
