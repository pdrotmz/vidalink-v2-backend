package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeactivateRewardUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private DeactivateRewardUseCaseImpl useCase;

    @Test
    void shouldReturnDeactivateReward() {
        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description test");
        reward.setStock(120);
        reward.setActive(true);

        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));

        useCase.execute(reward.getId());

        verify(rewardRepository).findById(rewardId);
    }

    @Test
    void shouldThrowsNotFoundByIdException() {
        UUID id = UUID.randomUUID();

        when(rewardRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RewardNotFoundByIdException.class, () -> useCase.execute(id));

        verify(rewardRepository).findById(id);
    }
}
