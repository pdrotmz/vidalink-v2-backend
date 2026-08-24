package com.vidalink.healthcare.gamification.application.usecase.level;

import com.vidalink.healthcare.gamification.entity.dto.response.level.UserLevelResponse;

import java.util.UUID;

public interface GetUserLevelUseCase {

    UserLevelResponse execute(UUID userId);
}
