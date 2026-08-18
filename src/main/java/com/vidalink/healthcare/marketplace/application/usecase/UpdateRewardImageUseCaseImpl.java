package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.application.port.out.FileStorage;
import com.vidalink.healthcare.marketplace.domain.exception.ImageEmptyException;
import com.vidalink.healthcare.marketplace.domain.exception.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
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
