package com.vidalink.healthcare.gamification.infrastructure.persistence.adapter.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.domain.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.userbadge.JpaUserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserBadgeRepositoryAdapter implements UserBadgeRepository {

    private final JpaUserBadgeRepository userBadgeRepository;

    @Override
    public UserBadge save(UserBadge userBadge) {
        return userBadgeRepository.save(userBadge);
    }

    @Override
    public List<UserBadge> findByUserId(UUID userId) {
        return userBadgeRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndBadge(UUID userId, Badge badge) {
        return userBadgeRepository.existsByUserIdAndBadge(userId, badge);
    }
}
