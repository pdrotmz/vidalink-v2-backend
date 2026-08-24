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


        User user = new User();

        Redemption redemption = new Redemption();
        redemption.setId(UUID.randomUUID());
        redemption.setIdUser(user.getId());
        redemption.setIdReward(UUID.randomUUID());
        redemption.setAmount(2);
        redemption.setCreatedAt(LocalDateTime.now());

        when(redemptionRepository.findByIdUser(user.getId()))
                .thenReturn(Optional.of(redemption));

        RedemptionResponse response = useCase.execute(user.getId());

        assertNotNull(response);
        assertEquals(user.getId(), response.idUser());


        verify(redemptionRepository).findByIdUser(user.getId());
    }

    @Test
    void shouldThrowExceptionWhenRedemptionByUserIdDoesNotExist() {

        UUID userId = UUID.randomUUID();

        when(redemptionRepository.findByIdUser(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                RedemptionNotFoundByIdUserException.class,
                () -> useCase.execute(userId)
        );

        verify(redemptionRepository).findByIdUser(userId);
    }
}
