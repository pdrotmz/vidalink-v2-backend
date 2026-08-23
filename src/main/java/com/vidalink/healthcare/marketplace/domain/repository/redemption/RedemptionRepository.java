package com.vidalink.healthcare.marketplace.domain.repository.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RedemptionRepository {

    Redemption save(Redemption redemption);

    Optional<Redemption> findById(UUID id);

    Optional<Redemption> findByIdUser(UUID id);

    Optional<Redemption> findByIdReward(UUID id);

    List<Redemption> findAll();
}
