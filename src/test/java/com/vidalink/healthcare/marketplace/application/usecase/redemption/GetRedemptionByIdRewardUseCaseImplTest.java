package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRedemptionByIdRewardUseCaseImplTest {

    @Mock
    private RedemptionRepository repository;

    @InjectMocks
    private GetRedemptionByIdRewardUseCaseImpl useCase;

    @Test
    void shouldReturnRedemptionByRewardId() {

        UUID rewardId = UUID.randomUUID();

        Redemption redemption = new Redemption();
        redemption.setId(UUID.randomUUID());
        redemption.setIdUser(UUID.randomUUID());
        redemption.setIdReward(rewardId);
        redemption.setAmount(2);
        redemption.setCreatedAt(LocalDateTime.now());

        when(repository.findByIdReward(rewardId))
                .thenReturn(Optional.of(redemption));

        RedemptionResponse response = useCase.execute(rewardId);

        assertNotNull(response);
        assertEquals(rewardId, response.idReward());

        verify(repository).findByIdReward(rewardId);
    }

    @Test
    void shouldThrowExceptionWhenRedemptionByRewardIdDoesNotExist() {

        UUID rewardId = UUID.randomUUID();

        when(repository.findByIdReward(rewardId))
                .thenReturn(Optional.empty());

        assertThrows(
                RedemptionNotFoundByIdException.class,
                () -> useCase.execute(rewardId)
        );

        verify(repository).findByIdReward(rewardId);
    }
}
