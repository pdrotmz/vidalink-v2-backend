package com.vidalink.healthcare.marketplace.application.usecase.reward;

import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import com.vidalink.healthcare.marketplace.domain.exception.reward.ImageEmptyException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;

@ExtendWith(MockitoExtension.class)
public class UpdateRewardImageUseCaseImplTest {

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private UpdateRewardImageUseCaseImpl useCase;

    @Test
    void shouldUpdateRewardImageSuccessfully() throws Exception {

        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setName("GIFT CARD LOL");
        reward.setDescription("Description Test");
        reward.setStock(100);
        reward.setActive(true);
        reward.setImagePath("rewards/old-image.png");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new-image.png",
                IMAGE_PNG_VALUE,
                "fake-image".getBytes()
        );

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(fileStorage.upload(
                any(InputStream.class),
                eq("rewards/" + rewardId),
                eq(IMAGE_PNG_VALUE)
        )).thenReturn("rewards/" + rewardId);

        when(rewardRepository.save(any(Reward.class)))
                .thenReturn(reward);

        RewardResponse response = useCase.execute(rewardId, image);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(rewardId);
        assertThat(response.image()).isEqualTo("rewards/" + rewardId);

        verify(rewardRepository).findById(rewardId);

        verify(fileStorage).upload(
                any(InputStream.class),
                eq("rewards/" + rewardId),
                eq(IMAGE_PNG_VALUE)
        );

        verify(rewardRepository).save(reward);
    }

    @Test
    void shouldThrowExceptionWhenImageIsEmpty() {
        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);

        MultipartFile image = mock(MultipartFile.class);

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

        when(image.isEmpty())
                .thenReturn(true);

        assertThatThrownBy(() ->
                useCase.execute(rewardId, image)
        )
                .isInstanceOf(ImageEmptyException.class)
                .hasMessage("Image cannot be empty");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenImageUploadFails() throws Exception {
        UUID rewardId = UUID.randomUUID();

        Reward reward = new Reward();
        reward.setId(rewardId);

        MultipartFile image = mock(MultipartFile.class);

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.of(reward));

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
                useCase.execute(rewardId, image)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("MinIO error");
    }


    @Test
    void shouldThrowWhenRewardDoesNotExist() {

        UUID rewardId = UUID.randomUUID();

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image".getBytes()
        );

        when(rewardRepository.findById(rewardId))
                .thenReturn(Optional.empty());

        assertThrows(
                RewardNotFoundByIdException.class,
                () -> useCase.execute(rewardId, image)
        );

        verify(fileStorage, never()).upload(
                any(),
                anyString(),
                anyString()
        );

        verify(rewardRepository, never()).save(any());
    }
}
