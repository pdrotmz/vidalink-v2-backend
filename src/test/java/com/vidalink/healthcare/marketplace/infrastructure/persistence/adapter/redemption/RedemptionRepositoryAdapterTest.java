package com.vidalink.healthcare.marketplace.infrastructure.persistence.adapter.redemption;

import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jpa.redemption.JpaRedemptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedemptionRepositoryAdapterTest {

    @Mock
    private JpaRedemptionRepository redemptionRepository;

    @InjectMocks
    private RedemptionRepositoryAdapter adapter;

    @Test
    void shouldSaveRedemption() {

        Redemption redemption = new Redemption();

        when(redemptionRepository.save(redemption)).thenReturn(redemption);

        Redemption result = adapter.save(redemption);

        assertEquals(redemption, result);

        verify(redemptionRepository).save(redemption);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();

        Optional<Redemption> redemption = Optional.of(new Redemption());

        when(redemptionRepository.findById(id)).thenReturn(redemption);

        Optional<Redemption> result = adapter.findById(id);

        assertEquals(redemption, result);

        verify(redemptionRepository).findById(id);
    }

    @Test
    void shouldFindByIdUser() {
        UUID id = UUID.randomUUID();

        Optional<Redemption> redemption = Optional.of(new Redemption());

        when(redemptionRepository.findByIdUser(id)).thenReturn(redemption);

        Optional<Redemption> result = adapter.findByIdUser(id);

        assertEquals(redemption, result);

        verify(redemptionRepository).findByIdUser(id);
    }

    @Test
    void shouldFindByIdReward() {
        UUID id = UUID.randomUUID();

        Optional<Redemption> redemption = Optional.of(new Redemption());

        when(redemptionRepository.findByIdReward(id)).thenReturn(redemption);

        Optional<Redemption> result = adapter.findByIdReward(id);

        assertEquals(redemption, result);

        verify(redemptionRepository).findByIdReward(id);
    }

    @Test
    void shouldFindAll() {

        List<Redemption> rewards = List.of(new Redemption(), new Redemption());

        when(redemptionRepository.findAll()).thenReturn(rewards);

        List<Redemption> result = adapter.findAll();

        assertNotNull(result);
        assertEquals(rewards, result);
        assertEquals(2, result.size());

        verify(redemptionRepository).findAll();
    }
}
