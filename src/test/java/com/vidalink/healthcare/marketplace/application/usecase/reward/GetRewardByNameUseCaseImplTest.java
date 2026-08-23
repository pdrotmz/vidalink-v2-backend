package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByNameException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetRewardByNameUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private GetRewardByNameUseCaseImpl useCase;

    @Test
    void shouldReturnRewardByName() {

        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description Test");
        reward.setStock(100);
        reward.setActive(true);

        when(rewardRepository.findByName(reward.getName())).thenReturn(Optional.of(reward));

        RewardResponse result = useCase.execute(reward.getName());

        assertEquals("GIFT CARD LOL", result.name());

        verify(rewardRepository).findByName(result.name());

        verify(rewardRepository).findByName(reward.getName());
    }

    @Test
    void shouldThrowsRewardNotFoundByName() {
        String name = "SPOTIFY PRO CODE";

        when(rewardRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(RewardNotFoundByNameException.class, () -> useCase.execute(name));

        verify(rewardRepository).findByName(name);
    }
}
