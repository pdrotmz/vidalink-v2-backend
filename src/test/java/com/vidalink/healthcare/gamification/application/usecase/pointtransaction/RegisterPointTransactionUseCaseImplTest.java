package com.vidalink.healthcare.gamification.application.usecase.pointtransaction;

import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPointTransactionUseCaseImplTest {

    @Mock
    private PointTransactionRepository pointTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterPointTransactionUseCaseImpl useCase;

    @Test
    void shouldRegisterPointTransaction() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        useCase.execute(
                userId,
                100,
                PointTransactionType.CREDIT,
                PointTransactionSource.ASSESSMENT
        );

        ArgumentCaptor<PointTransaction> transactionCaptor =
                ArgumentCaptor.forClass(PointTransaction.class);

        verify(pointTransactionRepository)
                .save(transactionCaptor.capture());

        PointTransaction transaction =
                transactionCaptor.getValue();

        assertEquals(userId, transaction.getUserId());
        assertEquals(100, transaction.getAmount());
        assertEquals(PointTransactionType.CREDIT, transaction.getType());
        assertEquals(
                PointTransactionSource.ASSESSMENT,
                transaction.getSource()
        );
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(
                        userId,
                        100,
                        PointTransactionType.CREDIT,
                        PointTransactionSource.ASSESSMENT
                )
        );

        verify(pointTransactionRepository, never())
                .save(any());
    }
}
