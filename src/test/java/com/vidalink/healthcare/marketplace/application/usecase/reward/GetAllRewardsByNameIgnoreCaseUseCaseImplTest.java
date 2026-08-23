package com.vidalink.healthcare.marketplace.application.usecase;

import com.vidalink.healthcare.marketplace.application.dto.response.RewardResponse;
import com.vidalink.healthcare.marketplace.application.usecase.reward.GetAllRewardsByNameIgnoreCaseUseCaseImpl;
import com.vidalink.healthcare.marketplace.domain.model.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllRewardsByNameIgnoreCaseUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private GetAllRewardsByNameIgnoreCaseUseCaseImpl useCase;

    @Test
    void shouldReturnRewardsByName() {

        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description test");
        reward.setStock(120);
        reward.setActive(true);

        when(rewardRepository.findByNameContainingIgnoreCase("gift"))
                .thenReturn(List.of(reward));

        List<RewardResponse> result = useCase.execute("gift");

        assertEquals(1, result.size());
        assertEquals("GIFT CARD LOL", result.getFirst().name());

        verify(rewardRepository).findByNameContainingIgnoreCase("gift");
    }
}
