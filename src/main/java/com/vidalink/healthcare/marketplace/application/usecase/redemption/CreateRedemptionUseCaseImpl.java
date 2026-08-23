package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionAmountUnderThanZeroException;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RewardInsufficientStockException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateRedemptionUseCaseImpl implements CreateRedemptionUseCase{

    private final RedemptionRepository redemptionRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;

    @Transactional
    @Override
    public RedemptionResponse execute(CreateRedemptionRequest request) {

        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.idUser()));

        Reward reward = rewardRepository.findById(request.idReward())
                .orElseThrow(() -> new RewardNotFoundByIdException(request.idReward()));

        if(request.amount() <= 0) {
            throw new RedemptionAmountUnderThanZeroException("Redemption amount must be greater than zero");
        }

        if (reward.getStock() < request.amount()) {
            throw new RewardInsufficientStockException("Insufficient reward stock");
        }

        reward.setStock(reward.getStock() - request.amount());

        Redemption redemption = new Redemption();
        redemption.setIdUser(user.getId());
        redemption.setIdReward(reward.getId());
        redemption.setAmount(request.amount());
        redemption.setCreatedAt(LocalDateTime.now());

        Redemption savedRedemption = redemptionRepository.save(redemption);

        return RedemptionResponse.from(savedRedemption);
    }
}
