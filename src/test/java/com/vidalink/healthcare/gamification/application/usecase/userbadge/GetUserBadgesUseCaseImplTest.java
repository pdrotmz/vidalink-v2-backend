package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.entity.dto.response.userbadge.UserBadgeResponse;
import com.vidalink.healthcare.gamification.entity.domain.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;
import com.vidalink.healthcare.gamification.entity.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserBadgesUseCaseImplTest {

    @Mock
    private UserBadgeRepository userBadgeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserBadgesUseCaseImpl useCase;

    @Test
    void shouldReturnUserBadges() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        UserBadge firstBadge = new UserBadge();
        firstBadge.setId(UUID.randomUUID());
        firstBadge.setUserId(userId);
        firstBadge.setBadge(Badge.FIRST_CONTRIBUTION);
        firstBadge.setEarnedAt(LocalDateTime.now());

        UserBadge secondBadge = new UserBadge();
        secondBadge.setId(UUID.randomUUID());
        secondBadge.setUserId(userId);
        secondBadge.setBadge(Badge.CONTRIBUTOR);
        secondBadge.setEarnedAt(LocalDateTime.now());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userBadgeRepository.findByUserId(userId))
                .thenReturn(List.of(firstBadge, secondBadge));

        List<UserBadgeResponse> response = useCase.execute(userId);

        assertEquals(2, response.size());

        assertEquals(
                Badge.FIRST_CONTRIBUTION,
                response.getFirst().badge()
        );

        assertEquals(
                Badge.CONTRIBUTOR,
                response.get(1).badge()
        );

        verify(userBadgeRepository).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoBadges() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userBadgeRepository.findByUserId(userId))
                .thenReturn(Collections.emptyList());

        List<UserBadgeResponse> response = useCase.execute(userId);

        assertTrue(response.isEmpty());

        verify(userBadgeRepository).findByUserId(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(userId)
        );

        verify(userBadgeRepository, never())
                .findByUserId(any());
    }
}
