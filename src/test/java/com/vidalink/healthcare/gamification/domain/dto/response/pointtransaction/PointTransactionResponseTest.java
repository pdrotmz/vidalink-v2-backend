package com.vidalink.healthcare.gamification.domain.dto.response.pointtransaction;

import com.vidalink.healthcare.gamification.application.dto.response.pointtransaction.PointTransactionResponse;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.domain.enums.pointtransaction.PointTransactionType;
import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PointTransactionResponseTest {

    @Test
    void shouldMapFromPointTransaction() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Integer amount = 100;
        PointTransactionType type = PointTransactionType.CREDIT;
        PointTransactionSource source = PointTransactionSource.ASSESSMENT;
        LocalDateTime createdAt = LocalDateTime.now();

        PointTransaction transaction = mock(PointTransaction.class);
        when(transaction.getId()).thenReturn(id);
        when(transaction.getUserId()).thenReturn(userId);
        when(transaction.getAmount()).thenReturn(amount);
        when(transaction.getType()).thenReturn(type);
        when(transaction.getSource()).thenReturn(source);
        when(transaction.getCreatedAt()).thenReturn(createdAt);

        PointTransactionResponse result = PointTransactionResponse.from(transaction);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(userId, result.userId());
        assertEquals(amount, result.amount());
        assertEquals(type, result.type());
        assertEquals(source, result.source());
        assertEquals(createdAt, result.createdAt());
    }
}
