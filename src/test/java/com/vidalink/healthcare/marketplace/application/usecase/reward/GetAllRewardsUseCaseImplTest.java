package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
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
public class GetAllRewardsUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private GetAllRewardsUseCaseImpl useCase;

    @Test
    void shouldReturnAllRewards() {
        UUID rewardID = UUID.randomUUID();

        Reward firstReward = new Reward();
        firstReward.setId(rewardID);
        firstReward.setName("GIFT CARD LOL");
        firstReward.setDescription("Description test");
        firstReward.setStock(120);
        firstReward.setActive(true);

        Reward secondReward = new Reward();
        secondReward.setId(rewardID);
        secondReward.setName("GIFT CARD YOUTUBE PREMIUM");
        firstReward.setDescription("Description test 2");
        secondReward.setStock(150);
        secondReward.setActive(true);

        when(rewardRepository.findAll()).thenReturn(List.of(firstReward, secondReward));

        List<RewardResponse> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("GIFT CARD LOL", result.get(0).name());
        assertEquals("GIFT CARD YOUTUBE PREMIUM", result.get(1).name());

        verify(rewardRepository).findAll();
    }
}
