package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionAmountUnderThanZeroException;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RewardInsufficientStockException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRedemptionUseCaseImplTest {

    @Mock
    private RedemptionRepository redemptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private CreateRedemptionUseCaseImpl useCase;

    @Test
    void shouldCreateRedemptionSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        2
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setStock(10);

        Redemption redemption = new Redemption();
        redemption.setId(UUID.randomUUID());
        redemption.setIdUser(userId);
        redemption.setIdReward(rewardId);
        redemption.setAmount(2);
        redemption.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(redemptionRepository.save(any(Redemption.class)))
                .thenReturn(redemption);

        RedemptionResponse response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(redemption.getId(), response.id());
        assertEquals(userId, response.idUser());
        assertEquals(rewardId, response.idReward());
        assertEquals(2, response.amount());

        assertEquals(8, reward.getStock());

        verify(userRepository).findById(userId);
        verify(rewardRepository).findById(rewardId);
        verify(redemptionRepository).save(any(Redemption.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        2
                );

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(request)
        );

        verify(userRepository).findById(userId);

        verifyNoInteractions(
                rewardRepository,
                redemptionRepository
        );
    }

    @Test
    void shouldThrowExceptionWhenRewardDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        2
                );

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.empty());

        assertThrows(
                RewardNotFoundByIdException.class,
                () -> useCase.execute(request)
        );

        verify(userRepository).findById(userId);
        verify(rewardRepository).findById(rewardId);

        verifyNoInteractions(redemptionRepository);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        0
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setStock(10);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        assertThrows(
                RedemptionAmountUnderThanZeroException.class,
                () -> useCase.execute(request)
        );

        verifyNoInteractions(redemptionRepository);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        -1
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setStock(10);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        assertThrows(
                RedemptionAmountUnderThanZeroException.class,
                () -> useCase.execute(request)
        );

        verifyNoInteractions(redemptionRepository);
    }

    @Test
    void shouldThrowExceptionWhenRewardHasInsufficientStock() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        userId,
                        rewardId,
                        3
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setStock(2);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        assertThrows(
                RewardInsufficientStockException.class,
                () -> useCase.execute(request)
        );

        assertEquals(2, reward.getStock());

        verifyNoInteractions(redemptionRepository);
    }
}
