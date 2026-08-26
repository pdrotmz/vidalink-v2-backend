package com.vidalink.healthcare.marketplace.application.usecase.redemption;

import com.vidalink.healthcare.marketplace.application.dto.response.redemption.RedemptionResponse;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
import com.vidalink.healthcare.marketplace.domain.repository.redemption.RedemptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllRedemptionsUseCaseImplTest {

    @Mock
    private RedemptionRepository repository;

    @InjectMocks
    private GetAllRedemptionsUseCaseImpl useCase;

    @Test
    void shouldReturnAllRedemptions() {

        Redemption firstRedemption = new Redemption();
        firstRedemption.setId(UUID.randomUUID());

        Redemption secondRedemption = new Redemption();
        secondRedemption.setId(UUID.randomUUID());

        when(repository.findAll())
                .thenReturn(List.of(
                        firstRedemption,
                        secondRedemption
                ));

        List<RedemptionResponse> responses =
                useCase.execute();

        assertEquals(2, responses.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoRedemptions() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<RedemptionResponse> responses =
                useCase.execute();

        assertTrue(responses.isEmpty());

        verify(repository).findAll();
    }
}
