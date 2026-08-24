package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardAlreadyExistsByNameException;
import com.vidalink.healthcare.marketplace.domain.model.reward.Reward;
import com.vidalink.healthcare.marketplace.domain.repository.reward.RewardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateRewardUseCaseTest {

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private CreateRewardUseCaseImpl useCase;

    @Mock
    private FileStorage fileStorage;

    @Test
    void shouldCreateRewardSuccessfully() throws Exception {

        UUID rewardId = UUID.randomUUID();

        CreateRewardRequest request = new CreateRewardRequest(
                "GIFT CARD LOL",
                "Description Test",
                100
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "reward.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        when(rewardRepository.save(any())).thenAnswer(invocation -> {
            Reward savedReward = invocation.getArgument(0);
            savedReward.setId(rewardId);
            savedReward.setActive(true);
            return savedReward;
        });

        when(fileStorage.upload(
                any(InputStream.class),
                anyString(),
                anyString()
        )).thenReturn("rewards/" + rewardId);

        RewardResponse response = useCase.execute(request, image);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(rewardId);
        assertThat(response.name()).isEqualTo("GIFT CARD LOL");
        assertThat(response.description()).isEqualTo("Description Test");
        assertThat(response.stock()).isEqualTo(100);

        verify(rewardRepository, times(2)).save(any(Reward.class));

        verify(fileStorage).upload(
                any(InputStream.class),
                eq("rewards/" + rewardId),
                eq(MediaType.IMAGE_PNG_VALUE)
        );
    }

    @Test
    void shouldCreateRewardWithoutImage() {
        CreateRewardRequest request = new CreateRewardRequest(
                "Reward",
                "Description",
                100
        );

        Reward savedReward = new Reward();
        savedReward.setId(UUID.randomUUID());
        savedReward.setName(request.name());
        savedReward.setDescription(request.description());
        savedReward.setStock(request.stock());
        savedReward.setActive(true);

        when(rewardRepository.existsByName(request.name())).thenReturn(false);
        when(rewardRepository.save(any(Reward.class))).thenReturn(savedReward);

        RewardResponse response = useCase.execute(request, null);

        assertThat(response).isNotNull();
        verify(rewardRepository).save(any(Reward.class));
        verifyNoInteractions(fileStorage);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenImageUploadFails() throws Exception {
        CreateRewardRequest request = new CreateRewardRequest(
                "Reward",
                "Description",
                100
        );

        MultipartFile image = mock(MultipartFile.class);

        Reward reward = new Reward();
        reward.setId(UUID.randomUUID());
        reward.setName(request.name());

        when(rewardRepository.existsByName(request.name()))
                .thenReturn(false);

        when(rewardRepository.save(any(Reward.class)))
                .thenReturn(reward);

        when(image.isEmpty())
                .thenReturn(false);

        when(image.getInputStream())
                .thenReturn(new ByteArrayInputStream("image".getBytes()));

        when(image.getContentType())
                .thenReturn("image/png");

        when(fileStorage.upload(
                any(InputStream.class),
                anyString(),
                anyString()
        )).thenThrow(new RuntimeException("MinIO error"));

        assertThatThrownBy(() ->
                useCase.execute(request, image)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("MinIO error");
    }

    @Test
    void shouldThrowRewardAlreadyExistsByName() {
        CreateRewardRequest request = new CreateRewardRequest(
                "GIFT CARD LOL",
                "Description Test",
                100
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "reward.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        when(rewardRepository.existsByName("GIFT CARD LOL")).thenReturn(true);

        assertThrows(RewardAlreadyExistsByNameException.class, () -> useCase.execute(request, image));

        verify(rewardRepository, never()).save(any(Reward.class));
    }
}
