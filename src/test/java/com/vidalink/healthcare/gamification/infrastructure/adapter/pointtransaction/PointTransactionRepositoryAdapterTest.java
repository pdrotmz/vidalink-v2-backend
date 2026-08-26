package com.vidalink.healthcare.gamification.infrastructure.adapter.pointtransaction;

import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.infrastructure.persistence.adapter.pointtransaction.PointTransactionRepositoryAdapter;
import com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.pointtransaction.JpaPointTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointTransactionRepositoryAdapterTest {

    @Mock
    private JpaPointTransactionRepository transactionRepository;

    @InjectMocks
    private PointTransactionRepositoryAdapter adapter;

    @Test
    void shouldSavePointTransaction() {
        PointTransaction transaction = new PointTransaction();

        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        PointTransaction result = adapter.save(transaction);

        assertEquals(transaction, result);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();

        Optional<PointTransaction> transaction = Optional.of(new PointTransaction());

        when(transactionRepository.findById(id)).thenReturn(transaction);

        Optional<PointTransaction> result = adapter.findById(id);

        assertEquals(transaction, result);

        verify(transactionRepository).findById(id);
    }

    @Test
    void shouldFindByUserId() {
        UUID userId = UUID.randomUUID();

        List<PointTransaction> transactions = List.of(new PointTransaction());

        when(transactionRepository.findByUserId(userId)).thenReturn(transactions);

        List<PointTransaction> result = adapter.findByUserId(userId);

        assertEquals(transactions, result);

        verify(transactionRepository).findByUserId(userId);
    }
}
