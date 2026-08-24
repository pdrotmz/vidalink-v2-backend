package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.domain.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwardBadgeUseCaseImpl implements AwardBadgeUseCase {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    @Override
    public void execute(UUID userId, Badge badge) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        boolean alreadyHasBadge =
                userBadgeRepository.existsByUserIdAndBadge(
                        user.getId(),
                        badge
                );

        if (alreadyHasBadge) {
            return;
        }

        UserBadge userBadge = new UserBadge();
        userBadge.setUserId(user.getId());
        userBadge.setBadge(badge);

        userBadgeRepository.save(userBadge);
    }
}