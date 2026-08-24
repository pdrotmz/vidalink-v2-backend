package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;

import com.vidalink.healthcare.gamification.entity.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.entity.domain.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionSource;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserPointTransactionsUseCaseImplTest {

    @Mock
    private PointTransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserPointTransactionsUseCaseImpl useCase;

    @Test
    void shouldReturnUserPointTransactions() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        PointTransaction creditTransaction = new PointTransaction();
        creditTransaction.setId(UUID.randomUUID());
        creditTransaction.setUserId(userId);
        creditTransaction.setAmount(100);
        creditTransaction.setType(PointTransactionType.CREDIT);
        creditTransaction.setSource(PointTransactionSource.ASSESSMENT);
        creditTransaction.setCreatedAt(LocalDateTime.now());

        PointTransaction debitTransaction = new PointTransaction();
        debitTransaction.setId(UUID.randomUUID());
        debitTransaction.setUserId(userId);
        debitTransaction.setAmount(30);
        debitTransaction.setType(PointTransactionType.DEBIT);
        debitTransaction.setSource(PointTransactionSource.MARKETPLACE);
        debitTransaction.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(List.of(creditTransaction, debitTransaction));

        List<PointTransactionResponse> response =
                useCase.execute(userId);

        assertEquals(2, response.size());

        assertEquals(
                creditTransaction.getId(),
                response.getFirst().id()
        );

        assertEquals(
                debitTransaction.getId(),
                response.get(1).id()
        );

        verify(transactionRepository)
                .findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTransactions() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(transactionRepository.findByUserId(userId))
                .thenReturn(Collections.emptyList());

        List<PointTransactionResponse> response =
                useCase.execute(userId);

        assertTrue(response.isEmpty());

        verify(transactionRepository)
                .findByUserId(userId);
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
