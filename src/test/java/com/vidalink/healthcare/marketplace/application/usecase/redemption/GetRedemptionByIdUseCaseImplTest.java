package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.RedemptionNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetRedemptionByIdUseCaseImplTest {

    @Mock
    private RedemptionRepository repository;

    @InjectMocks
    private GetRedemptionByIdUseCaseImpl useCase;

    @Test
    void shouldFindById() {

        UUID id = UUID.randomUUID();

        Redemption redemption = new Redemption();
        redemption.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(redemption));

        RedemptionResponse response = useCase.execute(id);

        assertNotNull(response);
        assertEquals(id, response.id());

        verify(repository).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenIdNotFound() {
        UUID id = UUID.randomUUID();

        Redemption redemption = new Redemption();
        redemption.setId(id);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RedemptionNotFoundByIdException.class, () -> useCase.execute(id));

        verify(repository).findById(id);
    }
}
