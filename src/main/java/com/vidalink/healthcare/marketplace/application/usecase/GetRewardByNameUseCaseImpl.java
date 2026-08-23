package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.RewardNotFoundByNameException;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
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
