package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllRedemptionsUseCaseImpl implements GetAllRedemptionsUseCase{

    private final RedemptionRepository redemptionRepository;

    @Override
    public List<RedemptionResponse> execute() {
        return redemptionRepository.
                findAll()
                .stream()
                .map(RedemptionResponse::from)
                .toList();
    }
}
