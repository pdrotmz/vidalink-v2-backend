package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.request.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRewardUseCaseImpl {

    private final RewardRepository rewardRepository;

    public RewardResponse execute(UUID id, UpdateRewardRequest request) {

        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RewardNotFoundByIdException(id));

        reward.setName(request.name());
        reward.setDescription(request.description());
        reward.setStock(request.stock());

        Reward updatedReward = rewardRepository.save(reward);

        return RewardResponse.from(updatedReward);
    }
}
