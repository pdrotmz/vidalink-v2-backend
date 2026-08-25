package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdRewardException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRedemptionByIdRewardUseCaseImpl implements GetRedemptionByIdRewardUseCase{

    private final RedemptionRepository redemptionRepository;

    @Override
    public RedemptionResponse execute(UUID id) {
        Redemption redemption = redemptionRepository.findByIdReward(id)
                .orElseThrow(() -> new RedemptionNotFoundByIdRewardException("Redemption not found with id reward: " + id));

        return RedemptionResponse.from(redemption);
    }
}
