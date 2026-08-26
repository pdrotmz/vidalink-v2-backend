package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.application.usecase.points.GetUserPointsUseCaseImpl;
import com.vidalink.healthcare.gamification.application.usecase.pointtransaction.RegisterPointTransactionUseCaseImpl;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.exception.InsufficientPointsException;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.request.redemption.CreateRedemptionRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionQuanityUnderThanZeroException;
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

    @Mock
    private RegisterPointTransactionUseCaseImpl registerPointTransactionUseCase;

    @Mock
    private GetUserPointsUseCaseImpl getUserPointsUseCase;

    @InjectMocks
    private CreateRedemptionUseCaseImpl useCase;

    @Test
    void shouldCreateRedemptionSuccessfully() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        rewardId,
                        2
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setPointsCost(120);
        reward.setStock(10);

        Redemption redemption = new Redemption();
        redemption.setId(UUID.randomUUID());
        redemption.setIdUser(userId);
        redemption.setIdReward(rewardId);
        redemption.setQuantity(2);
        redemption.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(getUserPointsUseCase.execute(userId))
                .thenReturn(new UserPointsResponse(userId, 500));

        when(redemptionRepository.save(any(Redemption.class)))
                .thenReturn(redemption);

        RedemptionResponse response = useCase.execute(userId, request);

        assertNotNull(response);
        assertEquals(redemption.getId(), response.id());
        assertEquals(userId, response.idUser());
        assertEquals(rewardId, response.idReward());
        assertEquals(2, response.quantity());

        assertEquals(8, reward.getStock());

        verify(userRepository).findById(userId);
        verify(rewardRepository).findById(rewardId);

        verify(getUserPointsUseCase).execute(userId);

        verify(registerPointTransactionUseCase).execute(
                eq(userId),
                eq(240),
                eq(PointTransactionType.DEBIT),
                eq(PointTransactionSource.MARKETPLACE)
        );

        verify(redemptionRepository).save(any(Redemption.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        rewardId,
                        2
                );

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(userId, request)
        );

        verify(userRepository).findById(userId);

        verifyNoInteractions(
                rewardRepository,
                redemptionRepository,
                getUserPointsUseCase,
                registerPointTransactionUseCase
        );
    }

    @Test
    void shouldThrowExceptionWhenRewardDoesNotExist() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
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
                () -> useCase.execute(userId, request)
        );

        verify(userRepository).findById(userId);
        verify(rewardRepository).findById(rewardId);

        verifyNoInteractions(
                redemptionRepository,
                getUserPointsUseCase,
                registerPointTransactionUseCase
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
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
                RedemptionQuanityUnderThanZeroException.class,
                () -> useCase.execute(userId, request)
        );

        verifyNoInteractions(
                redemptionRepository,
                getUserPointsUseCase,
                registerPointTransactionUseCase
        );
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
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
                RedemptionQuanityUnderThanZeroException.class,
                () -> useCase.execute(userId, request)
        );

        verifyNoInteractions(
                redemptionRepository,
                getUserPointsUseCase,
                registerPointTransactionUseCase
        );
    }

    @Test
    void shouldThrowExceptionWhenRewardHasInsufficientStock() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
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
                () -> useCase.execute(userId, request)
        );

        assertEquals(2, reward.getStock());

        verifyNoInteractions(
                redemptionRepository,
                getUserPointsUseCase,
                registerPointTransactionUseCase
        );
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotHaveEnoughPoints() {

        UUID userId = UUID.randomUUID();
        UUID rewardId = UUID.randomUUID();

        CreateRedemptionRequest request =
                new CreateRedemptionRequest(
                        rewardId,
                        2
                );

        User user = new User();
        user.setId(userId);

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setPointsCost(100);
        reward.setStock(10);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(getUserPointsUseCase.execute(userId))
                .thenReturn(new UserPointsResponse(userId, 100));

        assertThrows(
                InsufficientPointsException.class,
                () -> useCase.execute(userId, request)
        );

        assertEquals(10, reward.getStock());

        verify(getUserPointsUseCase).execute(userId);

        verifyNoInteractions(
                redemptionRepository,
                registerPointTransactionUseCase
        );
    }
}
