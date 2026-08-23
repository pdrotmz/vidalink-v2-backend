package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRewardByIdUseCaseImpl {

    private final RewardRepository rewardRepository;

    public RewardResponse execute(UUID rewardId) {

    Reward reward = rewardRepository.findById(rewardId)
            .orElseThrow(() -> new RewardNotFoundByIdException(rewardId));

    return RewardResponse.from(reward);
    }
}
