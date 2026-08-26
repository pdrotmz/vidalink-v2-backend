package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.ImageEmptyException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRewardImageUseCaseImpl {

    private final RewardRepository rewardRepository;
    private final FileStorage fileStorage;

    public RewardResponse execute(UUID rewardId, MultipartFile image) {

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RewardNotFoundByIdException(rewardId));

        if (image == null || image.isEmpty()) {
            throw new ImageEmptyException("Image cannot be empty");
        }

        try {

            String imagePath = "rewards/" + rewardId;

            String path = fileStorage.upload(
                    image.getInputStream(),
                    imagePath,
                    image.getContentType()
            );

            reward.setImagePath(path);

            Reward updatedReward = rewardRepository.save(reward);

            return RewardResponse.from(updatedReward);

        } catch (IOException exception) {
            throw new RuntimeException("Could not upload reward image", exception);
        }
    }
}
