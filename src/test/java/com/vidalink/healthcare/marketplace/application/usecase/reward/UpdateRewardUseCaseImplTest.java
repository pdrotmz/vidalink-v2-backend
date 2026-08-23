package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateRewardUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private UpdateRewardUseCaseImpl useCase;

    @Test
    void shouldUpdateRewardSuccessfully() {
        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description Test");
        reward.setStock(100);
        reward.setActive(true);

        UpdateRewardRequest request = new UpdateRewardRequest(
                "GIFT CARD YOUTUBE",
                "Test Description",
                50
        );

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(rewardRepository.save(any(Reward.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RewardResponse updatedReward = useCase.execute(rewardId, request);

        assertThat(updatedReward.name())
                .isEqualTo("GIFT CARD YOUTUBE");

        assertThat(updatedReward.description())
                .isEqualTo("Test Description");

        assertThat(updatedReward.stock())
                .isEqualTo(50);

        assertThat(updatedReward.id())
                .isEqualTo(rewardId);

        verify(rewardRepository).findById(rewardId);
        verify(rewardRepository).save(reward);
    }

    @Test
    void shouldThrowsNotFoundException() {

        UUID id = UUID.randomUUID();

        UpdateRewardRequest request = new UpdateRewardRequest(
                "GIFT CARD YOUTUBE",
                "Test Description",
                50
        );

        when(rewardRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RewardNotFoundByIdException.class, () -> useCase.execute(id, request));

        verify(rewardRepository, times(1)).findById(id);

        verify(rewardRepository, never()).save(any(Reward.class));
    }
}
