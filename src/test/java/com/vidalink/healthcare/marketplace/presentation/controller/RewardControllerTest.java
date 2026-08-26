package com.vidalink.healthcare.marketplace.presentation.controller;

import com.vidalink.healthcare.marketplace.application.dto.request.reward.CreateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.request.reward.UpdateRewardRequest;
import com.vidalink.healthcare.marketplace.application.dto.response.reward.RewardResponse;
import com.vidalink.healthcare.marketplace.application.usecase.reward.*;
import com.vidalink.healthcare.marketplace.domain.exception.reward.ImageEmptyException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardAlreadyExistsByNameException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByNameException;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import com.vidalink.healthcare.marketplace.presentation.controller.reward.RewardController;
import com.vidalink.healthcare.shared.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RewardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RewardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    CreateRewardUseCaseImpl createRewardUseCase;

    @MockitoBean
    GetAllRewardsUseCaseImpl getAllRewardsUseCase;

    @MockitoBean
    GetAllRewardsByNameIgnoreCaseUseCaseImpl getAllRewardsByNameIgnoreCaseUseCase;

    @MockitoBean
    GetRewardByNameUseCaseImpl getRewardByNameUseCase;

    @MockitoBean
    GetRewardByIdUseCaseImpl getRewardByIdUseCase;

    @MockitoBean
    UpdateRewardUseCaseImpl updateRewardUseCase;

    @MockitoBean
    UpdateRewardImageUseCaseImpl updateRewardImageUseCase;

    @MockitoBean
    DeactivateRewardUseCaseImpl deactivateRewardUseCase;


    @Test
    void shouldCreateRewardSuccessfully() throws Exception {

        CreateRewardRequest request = new CreateRewardRequest(
                "GIFT CARD LOL",
                "Description Test",
                100,
                20
        );

        MockMultipartFile reward = new MockMultipartFile(
                "reward",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "reward.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        RewardResponse response = new RewardResponse(
                UUID.randomUUID(),
                "GIFT CARD LOL",
                "Description Test",
                100,
                100,
                "rewards/" + UUID.randomUUID(),
                true
        );

        when(createRewardUseCase.execute(
                any(CreateRewardRequest.class),
                any(MultipartFile.class)
        )).thenReturn(response);

        mockMvc.perform(
                        multipart("/api/rewards/create")
                                .file(reward)
                                .file(image)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("GIFT CARD LOL"))
                .andExpect(jsonPath("$.description").value("Description Test"))
                .andExpect(jsonPath("$.stock").value(100))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(createRewardUseCase).execute(
                any(CreateRewardRequest.class),
                any(MultipartFile.class)
        );
    }

    @Test
    void shouldGetRewardById() throws Exception {

        UUID rewardId = UUID.randomUUID();

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD LOL",
                "Description Test",
                120,
                120,
                "rewards/" + rewardId,
                true
        );

        when(getRewardByIdUseCase.execute(rewardId)).thenReturn(response);

        mockMvc.perform(
                        get("/api/rewards/id/{id}", rewardId)
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(rewardId.toString()))
                .andExpect(jsonPath("$.name").value("GIFT CARD LOL"))
                .andExpect(jsonPath("$.description").value("Description Test"))
                .andExpect(jsonPath("$.stock").value(120))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(getRewardByIdUseCase).execute(rewardId);
    }

    @Test
    void shouldGetRewardByName() throws Exception {

        UUID rewardId = UUID.randomUUID();

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD LOL",
                "Description Test",
                120,
                120,
                "rewards/" + rewardId,
                true
        );

        when(getRewardByNameUseCase.execute(response.name())).thenReturn(response);

        mockMvc.perform(
                        get("/api/rewards/name/{name}", response.name())
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("GIFT CARD LOL"))
                .andExpect(jsonPath("$.description").value("Description Test"))
                .andExpect(jsonPath("$.stock").value(120))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(getRewardByNameUseCase).execute(response.name());
    }

    @Test
    void shouldGetAllRewards() throws Exception {

        UUID rewardId = UUID.randomUUID();

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD LOL",
                "Description Test",
                120,
                12,
                "rewards/" + rewardId,
                true
        );

        when(getAllRewardsUseCase.execute()).thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/rewards")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("GIFT CARD LOL"));

        verify(getAllRewardsUseCase).execute();
    }

    @Test
    void shouldGetSearchRewardByName() throws Exception {

        UUID rewardId = UUID.randomUUID();

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD LOL",
                "Description Test",
                120,
                120,
                "rewards/" + rewardId,
                true
        );

        when(getAllRewardsByNameIgnoreCaseUseCase.execute("gift"))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/rewards/search")
                                .param("keyword", "gift")
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("GIFT CARD LOL"));

        verify(getAllRewardsByNameIgnoreCaseUseCase).execute("gift");
    }

    @Test
    void shouldUpdateRewardSuccessfully() throws Exception {

        UUID rewardId = UUID.randomUUID();

        UpdateRewardRequest request = new UpdateRewardRequest(
                "GIFT CARD YOUTUBE",
                "Description Test",
                50
        );

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD YOUTUBE",
                "Updated description",
                50,
                120,
                "rewards/" + rewardId,
                true
        );

        when(updateRewardUseCase.execute(rewardId, request))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/api/rewards/update/{id}", rewardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(rewardId.toString()))
                .andExpect(jsonPath("$.name").value("GIFT CARD YOUTUBE"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.stock").value(50))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(updateRewardUseCase).execute(rewardId, request);
    }

    @Test
    void shouldUpdateRewardImageSuccessfully() throws Exception {

        UUID rewardId = UUID.randomUUID();

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new-image.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image".getBytes()
        );

        RewardResponse response = new RewardResponse(
                rewardId,
                "GIFT CARD LOL",
                "Description Test",
                100,
                120,
                "rewards/" + rewardId,
                true
        );

        when(updateRewardImageUseCase.execute(
                eq(rewardId),
                any(MultipartFile.class)
        )).thenReturn(response);

        mockMvc.perform(
                        multipart("/api/rewards/id/{id}/image", rewardId)
                                .file(image)
                                .with(request -> {
                                    request.setMethod("PATCH");
                                    return request;
                                })
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(rewardId.toString()))
                .andExpect(jsonPath("$.image").value("rewards/" + rewardId));

        verify(updateRewardImageUseCase).execute(
                eq(rewardId),
                any(MultipartFile.class)
        );
    }

    @Test
    void shouldReturnBadRequestWhenImageIsEmpty() throws Exception {

        UUID rewardId = UUID.randomUUID();

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "empty.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[0]
        );

        doThrow(
                new ImageEmptyException("Image cannot be empty")
        ).when(updateRewardImageUseCase)
                .execute(eq(rewardId), any(MultipartFile.class));

        mockMvc.perform(
                        multipart("/api/rewards/id/{id}/image", rewardId)
                                .file(image)
                                .with(request -> {
                                    request.setMethod("PATCH");
                                    return request;
                                })
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Image cannot be empty"));

        verify(updateRewardImageUseCase)
                .execute(eq(rewardId), any(MultipartFile.class));
    }

    @Test
    void shouldDeactivateRewardSuccessfully() throws Exception {

        UUID rewardId = UUID.randomUUID();

        doNothing()
                .when(deactivateRewardUseCase)
                .execute(rewardId);

        mockMvc.perform(
                        patch("/api/rewards/id/{id}/deactivate", rewardId)
                )
                .andExpect(status().isNoContent());

        verify(deactivateRewardUseCase).execute(rewardId);
    }

    @Test
    void shouldReturn409WhenNameAlreadyExists() throws Exception {

        CreateRewardRequest request = new CreateRewardRequest(
                "GIFT CARD LOL",
                "Description Test",
                100,
                100
        );

        MockMultipartFile reward = new MockMultipartFile(
                "reward",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "reward.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        when(createRewardUseCase.execute(
                any(CreateRewardRequest.class),
                any(MultipartFile.class)
        )).thenThrow(
                new RewardAlreadyExistsByNameException("GIFT CARD LOL")
        );

        mockMvc.perform(
                        multipart("/api/rewards/create")
                                .file(reward)
                                .file(image)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn404WhenRewardNotFoundById() throws Exception {

        UUID rewardId = UUID.randomUUID();

        when(getRewardByIdUseCase.execute(rewardId))
                .thenThrow(
                        new RewardNotFoundByIdException(rewardId)
                );

        mockMvc.perform(
                        get("/api/rewards/id/{id}", rewardId)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenRewardNotFoundByName() throws Exception {

        String name = "GIFT CARD YOUTUBE";

        when(getRewardByNameUseCase.execute(name))
                .thenThrow(
                        new RewardNotFoundByNameException(name)
                );

        mockMvc.perform(
                        get("/api/rewards/name/{name}", name)
                )
                .andExpect(status().isNotFound());
    }
}