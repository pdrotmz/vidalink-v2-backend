package com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRedemptionRepository extends JpaRepository<Redemption, UUID> {

    Optional<Redemption> findById(UUID id);

    Optional<Redemption> findByIdUser(UUID id);

    Optional<Redemption> findByIdReward(UUID id);
}
