package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.application.dto.response.userbadge.UserBadgeResponse;

import java.util.List;
import java.util.UUID;

public interface GetUserBadgesUseCase {

    List<UserBadgeResponse> execute(UUID userId);

}
