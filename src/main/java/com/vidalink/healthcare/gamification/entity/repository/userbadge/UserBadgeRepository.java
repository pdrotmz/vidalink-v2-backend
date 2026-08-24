package com.vidalink.healthcare.gamification.entity.repository.userbadge;

import com.vidalink.healthcare.gamification.entity.domain.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;

import java.util.List;
import java.util.UUID;

public interface UserBadgeRepository {

    UserBadge save(UserBadge userBadge);

    List<UserBadge> findByUserId(UUID userId);

    boolean existsByUserIdAndBadge(UUID userId, Badge badge);
}
