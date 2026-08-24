package com.vidalink.healthcare.gamification.application.usecase.userbadge;

import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.domain.repository.userbadge.UserBadgeRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AwardBadgeUseCaseImplTest {

    @Mock
    private UserBadgeRepository userBadgeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AwardBadgeUseCaseImpl useCase;

    @Test
    void shouldAwardBadgeWhenUserDoesNotHaveIt() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userBadgeRepository.existsByUserIdAndBadge(
                userId,
                Badge.FIRST_CONTRIBUTION
        )).thenReturn(false);

        useCase.execute(userId, Badge.FIRST_CONTRIBUTION);

        ArgumentCaptor<UserBadge> badgeCaptor =
                ArgumentCaptor.forClass(UserBadge.class);

        verify(userBadgeRepository).save(badgeCaptor.capture());

        UserBadge userBadge = badgeCaptor.getValue();

        assertEquals(userId, userBadge.getUserId());
        assertEquals(Badge.FIRST_CONTRIBUTION, userBadge.getBadge());
    }

    @Test
    void shouldNotAwardBadgeWhenUserAlreadyHasIt() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userBadgeRepository.existsByUserIdAndBadge(
                userId,
                Badge.FIRST_CONTRIBUTION
        )).thenReturn(true);

        useCase.execute(userId, Badge.FIRST_CONTRIBUTION);

        verify(userBadgeRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(
                        userId,
                        Badge.FIRST_CONTRIBUTION
                )
        );

        verify(userBadgeRepository, never())
                .existsByUserIdAndBadge(any(), any());

        verify(userBadgeRepository, never())
                .save(any());
    }
}