package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdUserException;
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
class GetRedemptionByIdUserUseCaseImplTest {

    @Mock
    private RedemptionRepository redemptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetRedemptionByIdUserUseCaseImpl useCase;

    @Test
    void shouldReturnRedemptionByUserId() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Redemption redemption = new Redemption();
        redemption.setId(UUID.randomUUID());
        redemption.setIdUser(userId);
        redemption.setIdReward(UUID.randomUUID());
        redemption.setAmount(2);
        redemption.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(redemptionRepository.findByIdUser(userId))
                .thenReturn(Optional.of(redemption));

        RedemptionResponse response = useCase.execute(userId);

        assertNotNull(response);
        assertEquals(userId, response.idUser());

        verify(userRepository).findById(userId);
        verify(redemptionRepository).findByIdUser(userId);
    }

    @Test
    void shouldThrowExceptionWhenRedemptionByUserIdDoesNotExist() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(redemptionRepository.findByIdUser(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                RedemptionNotFoundByIdUserException.class,
                () -> useCase.execute(userId)
        );

        verify(userRepository).findById(userId);
        verify(redemptionRepository).findByIdUser(userId);
    }
}
