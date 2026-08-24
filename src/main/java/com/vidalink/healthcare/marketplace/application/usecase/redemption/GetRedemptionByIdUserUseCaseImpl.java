package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdUserException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRedemptionByIdUserUseCaseImpl implements GetRedemptionByIdUserUseCase{

    private final RedemptionRepository redemptionRepository;
    private final UserRepository userRepository;


    @Override
    public RedemptionResponse execute(UUID id) {

        Redemption redemption = redemptionRepository.findByIdUser(id)
                .orElseThrow(() -> new RedemptionNotFoundByIdUserException("Redemption not found with id user: " + id));

        return RedemptionResponse.from(redemption);
    }
}
