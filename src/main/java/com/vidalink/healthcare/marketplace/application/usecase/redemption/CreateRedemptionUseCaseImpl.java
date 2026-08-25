package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.application.usecase.points.GetUserPointsUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCaseImpl;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.exception.InsufficientPointsException;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionQuanityUnderThanZeroException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRedemptionUseCaseImpl implements CreateRedemptionUseCase {

    private final RedemptionRepository redemptionRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final RegisterPointTransactionUseCaseImpl registerPointTransactionUseCase;
    private final GetUserPointsUseCaseImpl getUserPointsUseCase;

    @Transactional
    @Override
    public RedemptionResponse execute(UUID userId, CreateRedemptionRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        Reward reward = rewardRepository.findById(request.idReward())
                .orElseThrow(() ->
                        new RewardNotFoundByIdException(request.idReward())
                );

        if (request.quantity() <= 0) {
            throw new RedemptionQuanityUnderThanZeroException(
                    "Redemption quantity must be greater than zero"
            );
        }

        if (reward.getStock() < request.quantity()) {
            throw new RewardInsufficientStockException(
                    "Insufficient reward stock"
            );
        }

        int pointsToDebit =
                reward.getPointsCost() * request.quantity();

        UserPointsResponse userPoints =
                getUserPointsUseCase.execute(user.getId());

        if (userPoints.balance() < pointsToDebit) {
            throw new InsufficientPointsException(
                    "User does not have enough points"
            );
        }

        reward.setStock(
                reward.getStock() - request.quantity()
        );

        Redemption redemption = new Redemption();
        redemption.setIdUser(user.getId());
        redemption.setIdReward(reward.getId());
        redemption.setQuantity(request.quantity());
        redemption.setCreatedAt(LocalDateTime.now());

        Redemption savedRedemption =
                redemptionRepository.save(redemption);

        registerPointTransactionUseCase.execute(
                user.getId(),
                pointsToDebit,
                PointTransactionType.DEBIT,
                PointTransactionSource.MARKETPLACE
        );

        return RedemptionResponse.from(savedRedemption);
    }
}


