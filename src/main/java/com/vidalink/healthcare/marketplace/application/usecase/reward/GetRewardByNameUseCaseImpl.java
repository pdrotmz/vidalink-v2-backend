package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByNameException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRewardByNameUseCaseImpl {

    private final RewardRepository rewardRepository;

    public RewardResponse execute(String rewardName) {

    Reward reward = rewardRepository.findByName(rewardName)
            .orElseThrow(() -> new RewardNotFoundByNameException(rewardName));

    return RewardResponse.from(reward);
    }
}
