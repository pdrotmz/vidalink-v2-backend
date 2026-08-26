package com.vidalink.healthcare.gamification.application.usecase.level;

import com.vidalink.healthcare.gamification.application.dto.response.level.UserLevelResponse;

import java.util.UUID;

public interface GetUserLevelUseCase {

    UserLevelResponse execute(UUID userId);
}
