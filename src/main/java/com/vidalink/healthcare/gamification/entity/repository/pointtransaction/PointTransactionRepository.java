package com.vidalink.healthcare.gamification.entity.repository.pointtransaction;

import com.vidalink.healthcare.gamification.entity.domain.pointtransaction.PointTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PointTransactionRepository {

    PointTransaction save(PointTransaction transaction);

    List<PointTransaction> findByUserId(UUID userId);

    Optional<PointTransaction> findById(UUID id);
}
