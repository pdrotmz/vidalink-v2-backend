package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardImageResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRewardImageUseCaseImpl implements GetRewardImageUseCase {

    private final RewardRepository rewardRepository;
    private final FileStorage fileStorage;

    public RewardImageResponse execute(UUID id) {

        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RewardNotFoundByIdException(id));

        InputStream inputStream = fileStorage.download(
                reward.getImagePath()
        );

        return new RewardImageResponse(inputStream);
    }
}