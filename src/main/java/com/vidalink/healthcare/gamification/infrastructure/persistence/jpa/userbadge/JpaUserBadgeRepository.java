package com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.userbadge;

import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaUserBadgeRepository extends JpaRepository<UserBadge, UUID> {

    List<UserBadge> findByUserId(UUID userId);

    boolean existsByUserIdAndBadge(UUID userId, Badge badge);
}
