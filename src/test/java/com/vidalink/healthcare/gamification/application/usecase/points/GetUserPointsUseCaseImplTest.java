package com.vidalink.healthcare.gamification.application.usecase.points;

import com.vidalink.healthcare.gamification.application.dto.response.points.UserPointsResponse;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserPointsUseCaseImplTest {

    @Mock
    private PointTransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserPointsUseCaseImpl useCase;

    @Test
    void shouldCalculateUserPoints() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PointTransaction creditTransaction = new PointTransaction();
        creditTransaction.setUserId(userId);
        creditTransaction.setAmount(100);
        creditTransaction.setType(PointTransactionType.CREDIT);

        PointTransaction debitTransaction = new PointTransaction();
        debitTransaction.setUserId(userId);
        debitTransaction.setAmount(30);
        debitTransaction.setType(PointTransactionType.DEBIT);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(List.of(creditTransaction, debitTransaction));

        UserPointsResponse response = useCase.execute(userId);

        assertEquals(userId, response.userId());
        assertEquals(70, response.balance());
    }

    @Test
    void shouldReturnZeroWhenUserHasNoTransactions() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(Collections.emptyList());

        UserPointsResponse response = useCase.execute(userId);

        assertEquals(userId, response.userId());
        assertEquals(0, response.balance());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(userId)
        );

        verify(transactionRepository, never())
                .findByUserId(any());
    }
}
