package com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaRewardRepository extends JpaRepository<Reward, UUID> {

    Optional<Reward> findByName(String name);

    List<Reward> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
