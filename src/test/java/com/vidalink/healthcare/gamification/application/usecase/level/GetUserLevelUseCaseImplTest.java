package com.vidalink.healthcare.gamification.application.usecase.level;

import com.vidalink.healthcare.gamification.entity.dto.response.level.UserLevelResponse;
import com.vidalink.healthcare.gamification.entity.domain.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.entity.enums.level.Level;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.entity.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserLevelUseCaseImplTest {

    @Mock
    private PointTransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserLevelUseCaseImpl useCase;

    @Test
    void shouldReturnBeginnerLevel() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PointTransaction transaction = new PointTransaction();
        transaction.setAmount(300);
        transaction.setType(PointTransactionType.CREDIT);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(List.of(transaction));

        UserLevelResponse response = useCase.execute(userId);

        assertEquals(userId, response.userId());
        assertEquals(Level.BEGINNER, response.level());
        assertEquals(300, response.totalPoints());
    }

    @Test
    void shouldReturnCorrectLevelBasedOnTotalPoints() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PointTransaction firstTransaction = new PointTransaction();
        firstTransaction.setAmount(600);
        firstTransaction.setType(PointTransactionType.CREDIT);

        PointTransaction secondTransaction = new PointTransaction();
        secondTransaction.setAmount(900);
        secondTransaction.setType(PointTransactionType.CREDIT);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(List.of(
                        firstTransaction,
                        secondTransaction
                ));

        UserLevelResponse response = useCase.execute(userId);

        assertEquals(Level.ADVANCED, response.level());
        assertEquals(1500, response.totalPoints());
    }

    @Test
    void shouldIgnoreDebitTransactionsWhenCalculatingLevel() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PointTransaction creditTransaction = new PointTransaction();
        creditTransaction.setAmount(1000);
        creditTransaction.setType(PointTransactionType.CREDIT);

        PointTransaction debitTransaction = new PointTransaction();
        debitTransaction.setAmount(800);
        debitTransaction.setType(PointTransactionType.DEBIT);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(List.of(
                        creditTransaction,
                        debitTransaction
                ));

        UserLevelResponse response = useCase.execute(userId);

        assertEquals(Level.ADVANCED, response.level());
        assertEquals(1000, response.totalPoints());
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
