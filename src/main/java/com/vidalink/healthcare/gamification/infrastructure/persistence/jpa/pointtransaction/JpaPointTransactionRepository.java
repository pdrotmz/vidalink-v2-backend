package com.vidalink.healthcare.gamification.infrastructure.persistence.jpa.pointtransaction;

import com.vidalink.healthcare.gamification.domain.model.pointtransaction.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPointTransactionRepository extends JpaRepository<PointTransaction, UUID> {

    List<PointTransaction> findByUserId(UUID id);
}
