package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllRewardsUseCaseImpl {

    private final RewardRepository rewardRepository;

    public List<RewardResponse> execute() {

        return rewardRepository.findAll()
                .stream()
                .map(RewardResponse::from)
                .toList();
    }
}
