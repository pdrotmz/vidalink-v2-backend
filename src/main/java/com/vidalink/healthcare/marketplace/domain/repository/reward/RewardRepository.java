package com.vidalink.healthcare.marketplace.domain.repository.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RewardRepository {

    Reward save(Reward reward);

    Optional<Reward> findById(UUID id);

    Optional<Reward> findByName(String name);

    boolean existsByName(String name);

    List<Reward> findAll();

    List<Reward> findByNameContainingIgnoreCase(String name);
}
