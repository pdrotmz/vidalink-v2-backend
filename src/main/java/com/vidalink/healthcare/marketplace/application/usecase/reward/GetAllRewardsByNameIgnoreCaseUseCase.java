package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;

import java.util.List;

public interface GetAllRewardsByNameIgnoreCaseUseCase {

    List<RewardResponse> execute();
}
