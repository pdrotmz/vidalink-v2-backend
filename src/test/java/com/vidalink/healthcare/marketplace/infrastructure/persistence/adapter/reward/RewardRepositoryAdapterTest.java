package com.vidalink.healthcare.marketplace.infrastructure.persistence.adapter.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.reward.JpaRewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardRepositoryAdapterTest {

    @Mock
    private JpaRewardRepository rewardRepository;

    @InjectMocks
    private RewardRepositoryAdapter adapter;

    @Test
    void shouldSaveReward() {

        Reward reward = new Reward();

        when(rewardRepository.save(reward)).thenReturn(reward);

        Reward result = adapter.save(reward);

        assertEquals(reward, result);

        verify(rewardRepository).save(reward);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();

        Optional<Reward> reward = Optional.of(new Reward());

        when(rewardRepository.findById(id)).thenReturn(reward);

        Optional<Reward> result = adapter.findById(id);

        assertEquals(reward, result);

        verify(rewardRepository).findById(id);
    }

    @Test
    void shouldFindByName() {

        String name = "GIFT CARD LOL";

        Optional<Reward> reward = Optional.of(new Reward());

        when(rewardRepository.findByName(name)).thenReturn(reward);

        Optional<Reward> result = adapter.findByName(name);

        assertEquals(reward, result);

        verify(rewardRepository).findByName(name);
    }

    @Test
    void shouldReturnWhenExistsByName() {

        String name = "GIFT CARD LOL";

        when(rewardRepository.existsByName(name)).thenReturn(true);

        boolean result = adapter.existsByName(name);

        assertTrue(result);

        verify(rewardRepository).existsByName(name);
    }

    @Test
    void shouldFindAll() {

        List<Reward> rewards = List.of(new Reward(), new Reward());

        when(rewardRepository.findAll()).thenReturn(rewards);

        List<Reward> result = adapter.findAll();

        assertNotNull(result);
        assertEquals(rewards, result);
        assertEquals(2, result.size());

        verify(rewardRepository).findAll();
    }

    @Test
    void shouldFindByNameContainingIgnoreCase() {

        String name = "GIFT CARD LOL";

        List<Reward> reward = List.of(new Reward());

        when(rewardRepository.findByNameContainingIgnoreCase(name)).thenReturn(reward);

        List<Reward> result = adapter.findByNameContainingIgnoreCase(name);

        assertEquals(reward, result);

        verify(rewardRepository).findByNameContainingIgnoreCase(name);
    }
}
