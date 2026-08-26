package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateRewardUseCaseImpl {

    private final RewardRepository rewardRepository;

    public void execute(UUID id) {

        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RewardNotFoundByIdException(id));

        reward.setActive(false);

        rewardRepository.save(reward);
    }
}
