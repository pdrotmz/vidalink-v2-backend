package com.vidalink.healthcare.marketplace.infrastructure.persistence.adapter.reward;

import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.reward.JpaRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RewardRepositoryAdapter implements RewardRepository {

    private final JpaRewardRepository jpaRewardRepository;

    @Override
    public Reward save(Reward reward) {
        return jpaRewardRepository.save(reward);
    }

    @Override
    public Optional<Reward> findById(UUID id) {
        return jpaRewardRepository.findById(id);
    }

    @Override
    public Optional<Reward> findByName(String name) {
        return jpaRewardRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRewardRepository.existsByName(name);
    }

    @Override
    public List<Reward> findAll() {
        return jpaRewardRepository.findAll();
    }

    @Override
    public List<Reward> findByNameContainingIgnoreCase(String name) {
        return jpaRewardRepository.findByNameContainingIgnoreCase(name);
    }
}
