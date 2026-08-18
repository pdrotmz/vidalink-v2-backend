package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
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
