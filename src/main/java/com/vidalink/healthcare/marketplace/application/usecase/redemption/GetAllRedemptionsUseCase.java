package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;

import java.util.List;

public interface GetAllRedemptionsUseCase {

    List<RedemptionResponse> execute();
}
