package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;

import java.util.UUID;

public interface AwardBadgeUseCase {

    void execute(UUID userId, Badge badge);
}
