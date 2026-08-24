package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;

import java.util.UUID;

public interface AwardBadgeUseCase {

    void execute(UUID userId, Badge badge);
}
