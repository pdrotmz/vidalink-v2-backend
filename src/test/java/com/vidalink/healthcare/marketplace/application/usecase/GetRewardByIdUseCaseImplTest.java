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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetRewardByIdUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private GetRewardByIdUseCaseImpl useCase;

    @Test
    void shouldReturnRewardById() {
        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description test");
        reward.setStock(120);
        reward.setActive(true);

        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));

        RewardResponse result = useCase.execute(rewardId);

        assertEquals(rewardId, result.id());
        assertEquals("GIFT CARD LOL", result.name());

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
