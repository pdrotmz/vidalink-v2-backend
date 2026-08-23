package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
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
