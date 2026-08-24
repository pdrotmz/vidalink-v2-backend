package com.vidalink.healthcare.gamification.infrastructure.adapter.userbadge;

import com.vidalink.healthcare.gamification.domain.model.userbadge.UserBadge;
import com.vidalink.healthcare.gamification.domain.enums.badge.Badge;
import com.vidalink.healthcare.gamification.infrastructure.persistence.adapter.userbadge.UserBadgeRepositoryAdapter;
import com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.userbadge.JpaUserBadgeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserBadgeRepositoryAdapterTest {

    @Mock
    private JpaUserBadgeRepository userBadgeRepository;

    @InjectMocks
    private UserBadgeRepositoryAdapter adapter;

    @Test
    void shouldSaveUserBadge() {
        UserBadge userBadge = new UserBadge();

        when(userBadgeRepository.save(userBadge))
                .thenReturn(userBadge);

        UserBadge result = adapter.save(userBadge);

        assertEquals(userBadge, result);

        verify(userBadgeRepository).save(userBadge);
    }

    @Test
    void shouldFindById() {
        UUID userId = UUID.randomUUID();

        List<UserBadge> userBadge = List.of(new UserBadge());

        when(userBadgeRepository.findByUserId(userId)).thenReturn(userBadge);

        List<UserBadge> result = adapter.findByUserId(userId);

        assertEquals(userBadge, result);

        verify(userBadgeRepository).findByUserId(userId);
    }

    @Test
    void shouldReturnTrueWhenExistsByUserIdAndBadge() {
        UUID userId = UUID.randomUUID();
        Badge badge = Badge.CONTRIBUTOR;

        when(userBadgeRepository.existsByUserIdAndBadge(userId, badge)).thenReturn(true);

        boolean result = adapter.existsByUserIdAndBadge(userId, badge);

        assertTrue(result);

        verify(userBadgeRepository).existsByUserIdAndBadge(userId, badge);
    }
}
