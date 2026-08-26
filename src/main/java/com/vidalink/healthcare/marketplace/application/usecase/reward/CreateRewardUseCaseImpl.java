package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardAlreadyExistsByNameException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CreateRewardUseCaseImpl {

    private final RewardRepository rewardRepository;
    private final FileStorage fileStorage;

    public RewardResponse execute(CreateRewardRequest request, MultipartFile image) {

        if (rewardRepository.existsByName(request.name())) {
            throw new RewardAlreadyExistsByNameException(request.name());
        }

        Reward reward = new Reward();

        reward.setName(request.name());
        reward.setDescription(request.description());
        reward.setPointsCost(request.pointCost());
        reward.setStock(request.stock());
        reward.setActive(true);

        Reward saveReward = rewardRepository.save(reward);

        if(image != null && !image.isEmpty()) {
            String imagePath = "rewards/" + saveReward.getId();

            try {
                String path = fileStorage.upload(
                        image.getInputStream(),
                        imagePath,
                        image.getContentType()
                );

                saveReward.setImagePath(path);
                saveReward = rewardRepository.save(saveReward);
            } catch (IOException exception) {
                throw new RuntimeException("Could not upload reward image", exception);
            }
        }
        return RewardResponse.from(saveReward);
    }
}
