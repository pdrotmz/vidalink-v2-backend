package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.application.dto.response.userbadge.UserBadgeResponse;
import com.vidalink.healthcare.gamification.domain.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserBadgesUseCaseImpl implements GetUserBadgesUseCase {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserBadgeResponse> execute(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return userBadgeRepository
                .findByUserId(user.getId())
                .stream()
                .map(userBadge ->
                        new UserBadgeResponse(
                                userBadge.getId(),
                                userBadge.getUserId(),
                                userBadge.getBadge(),
                                userBadge.getEarnedAt()
                        )
                )
                .toList();
    }
}
