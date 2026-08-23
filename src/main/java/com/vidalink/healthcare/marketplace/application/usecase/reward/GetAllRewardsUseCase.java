package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;

import java.util.List;

public interface GetAllRewardsUseCase {

    List<RewardResponse> execute();
}
