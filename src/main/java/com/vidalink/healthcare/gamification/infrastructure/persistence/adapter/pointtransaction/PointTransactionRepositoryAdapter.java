package com.vidalink.healthcare.gamification.infrastructure.persistence.adapter.pointtransaction;

import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import com.vidalink.healthcare.gamification.domain.repository.pointtransaction.PointTransactionRepository;
import com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.pointtransaction.JpaPointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PointTransactionRepositoryAdapter implements PointTransactionRepository {

    private final JpaPointTransactionRepository transactionRepository;

    @Override
    public PointTransaction save(PointTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<PointTransaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<PointTransaction> findByUserId(UUID id) {
        return transactionRepository.findByUserId(id);
    }
}
