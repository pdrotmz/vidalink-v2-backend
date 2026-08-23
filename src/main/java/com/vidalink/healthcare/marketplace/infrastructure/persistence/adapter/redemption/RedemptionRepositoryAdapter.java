package com.vidalink.healthcare.marketplace.infrastructure.persistence.adapter.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.redemption.JpaRedemptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RedemptionRepositoryAdapter implements RedemptionRepository {

    private final JpaRedemptionRepository redemptionRepository;

    @Override
    public Redemption save(Redemption redemption) {
        return redemptionRepository.save(redemption);
    }

    @Override
    public Optional<Redemption> findById(UUID id) {
        return redemptionRepository.findById(id);
    }

    @Override
    public Optional<Redemption> findByIdUser(UUID id) {
        return redemptionRepository.findByIdUser(id);
    }

    @Override
    public Optional<Redemption> findByIdReward(UUID id) {
        return redemptionRepository.findByIdReward(id);
    }

    @Override
    public List<Redemption> findAll() {
        return redemptionRepository.findAll();
    }
}
