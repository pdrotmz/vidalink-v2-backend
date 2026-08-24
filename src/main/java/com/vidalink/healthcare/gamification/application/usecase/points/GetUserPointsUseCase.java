package com.vidalink.healthcare.gamification.application.usecase.points;

import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;

import java.util.UUID;

public interface GetUserPointsUseCase {

    UserPointsResponse execute(UUID userId);
}
