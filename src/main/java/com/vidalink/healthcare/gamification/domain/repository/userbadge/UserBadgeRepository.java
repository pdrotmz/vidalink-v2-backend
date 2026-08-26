package com.vidalink.healthcare.gamification.domain.repository.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;

import java.util.List;
import java.util.UUID;

public interface UserBadgeRepository {

    UserBadge save(UserBadge userBadge);

    List<UserBadge> findByUserId(UUID userId);

    boolean existsByUserIdAndBadge(UUID userId, Badge badge);
}
