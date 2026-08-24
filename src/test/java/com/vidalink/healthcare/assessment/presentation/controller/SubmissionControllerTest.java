package com.vidalink.healthcare.assessment.presentation.controller;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.application.usecase.*;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionFileFormatNotAcceptedException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotSentException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.marketplace.infrastructure.persistence.jwt.JwtService;
import com.vidalink.healthcare.shared.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    SendSubmissionUseCaseImpl sendSubmissionUseCase;

    @MockitoBean
    GetAllSubmissionsUseCaseImpl getAllSubmissionsUseCase;

    @MockitoBean
    GetSubmissionByIdUseCaseImpl getSubmissionByIdUseCase;

    @MockitoBean
    GetSubmissionsByIdUserUseCaseImpl getSubmissionsByIdUserUseCase;

    @MockitoBean
    GetSubmissionsByStatusUseCaseImpl getSubmissionsByStatusUseCase;

    @MockitoBean
    UpdateStatusSubmissionUseCaseImpl updateStatusSubmissionUseCase;

    @MockitoBean
    DeleteSubmissionByIdUseCaseImpl deleteSubmissionByIdUseCase;


    @Test
    void shouldSendSubmissionSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "submission.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "fake-pdf-content".getBytes()
        );

        UUID submissionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime sentTime = LocalDateTime.now();

        SubmissionResponse response = new SubmissionResponse(
                submissionId,
                userId,
                sentTime,
                "submissions/" + submissionId,
                ValidationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(sendSubmissionUseCase.execute(
                eq("user@email.com"),
                any(MultipartFile.class)
        )).thenReturn(response);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "user@email.com",
                        null
                );

        mockMvc.perform(
                        multipart("/api/submissions/send")
                                .file(file)
                                .principal(authentication)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value(submissionId.toString()))
                .andExpect(jsonPath("$.idUser")
                        .value(userId.toString()))
                .andExpect(jsonPath("$.sentTime").exists())
                .andExpect(jsonPath("$.filePath")
                        .value("submissions/" + submissionId))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());


        verify(sendSubmissionUseCase).execute(
                eq("user@email.com"),
                any(MultipartFile.class)
        );
    }


    @Test
    void shouldGetAllSubmissions() throws Exception {

        SubmissionResponse response = new SubmissionResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                "submissions/test",
                ValidationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(getAllSubmissionsUseCase.execute())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/submissions")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(getAllSubmissionsUseCase).execute();
    }


    @Test
    void shouldGetSubmissionById() throws Exception {

        UUID submissionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        SubmissionResponse response = new SubmissionResponse(
                submissionId,
                userId,
                LocalDateTime.now(),
                "submissions/test",
                ValidationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(getSubmissionByIdUseCase.execute(submissionId))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/submissions/id/{id}", submissionId)
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id")
                        .value(submissionId.toString()))
                .andExpect(jsonPath("$.idUser")
                        .value(userId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));

        verify(getSubmissionByIdUseCase)
                .execute(submissionId);
    }


    @Test
    void shouldGetSubmissionsByUserId() throws Exception {

        UUID submissionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Submission submission = new Submission();

        submission.setId(submissionId);
        submission.setIdUser(userId);
        submission.setFile("submissions/" + submissionId);
        submission.setSentTime(LocalDateTime.now());
        submission.setStatus(ValidationStatus.PENDING);

        when(getSubmissionsByIdUserUseCase.execute(userId))
                .thenReturn(List.of(submission));

        mockMvc.perform(
                        get("/api/submissions/id/user/{id}", userId)
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id")
                        .value(submissionId.toString()))
                .andExpect(jsonPath("$[0].idUser")
                        .value(userId.toString()));

        verify(getSubmissionsByIdUserUseCase)
                .execute(userId);
    }


    @Test
    void shouldGetSubmissionsByStatus() throws Exception {

        SubmissionResponse response = new SubmissionResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                "submissions/test",
                ValidationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(getSubmissionsByStatusUseCase.execute(
                ValidationStatus.PENDING
        )).thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/submissions/status")
                                .param("status", "PENDING")
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status")
                        .value("PENDING"));

        verify(getSubmissionsByStatusUseCase)
                .execute(ValidationStatus.PENDING);
    }


    @Test
    void shouldApproveSubmissionSuccessfully() throws Exception {

        UUID submissionId = UUID.randomUUID();

        doNothing()
                .when(updateStatusSubmissionUseCase)
                .approved(submissionId);

        mockMvc.perform(
                        patch(
                                "/api/submissions/id/status/{id}/approve",
                                submissionId
                        )
                )
                .andExpect(status().isAccepted());

        verify(updateStatusSubmissionUseCase)
                .approved(submissionId);
    }


    @Test
    void shouldRejectSubmissionSuccessfully() throws Exception {

        UUID submissionId = UUID.randomUUID();

        doNothing()
                .when(updateStatusSubmissionUseCase)
                .rejected(submissionId);

        mockMvc.perform(
                        patch(
                                "/api/submissions/id/status/{id}/reject",
                                submissionId
                        )
                )
                .andExpect(status().isAccepted());

        verify(updateStatusSubmissionUseCase)
                .rejected(submissionId);
    }


    @Test
    void shouldDeleteSubmissionSuccessfully() throws Exception {

        UUID submissionId = UUID.randomUUID();

        doNothing()
                .when(deleteSubmissionByIdUseCase)
                .execute(submissionId);

        mockMvc.perform(
                        delete(
                                "/api/submissions/id/delete/{id}",
                                submissionId
                        )
                )
                .andExpect(status().isNoContent());

        verify(deleteSubmissionByIdUseCase)
                .execute(submissionId);
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "submission.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "fake-pdf-content".getBytes()
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "user@email.com",
                        null
                );

        doThrow(new UserNotFoundException(
                "User not found with email: user@email.com"
        ))
                .when(sendSubmissionUseCase)
                .execute(
                        eq("user@email.com"),
                        any(MultipartFile.class)
                );

        mockMvc.perform(
                        multipart("/api/submissions/send")
                                .file(file)
                                .principal(authentication)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("User not found with email: user@email.com"));
    }

    @Test
    void shouldReturnBadRequestWhenSubmissionCannotBeSent() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "submission.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "fake-pdf-content".getBytes()
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "user@email.com",
                        null
                );

        doThrow(new SubmissionNotSentException(
                "Submission file is required"
        ))
                .when(sendSubmissionUseCase)
                .execute(
                        eq("user@email.com"),
                        any(MultipartFile.class)
                );

        mockMvc.perform(
                        multipart("/api/submissions/send")
                                .file(file)
                                .principal(authentication)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Submission file is required"));
    }

    @Test
    void shouldReturnBadRequestWhenSubmissionFileIsNotPdf() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "submission.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-image-content".getBytes()
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        "user@email.com",
                        null
                );

        doThrow(new SubmissionFileFormatNotAcceptedException(
                "Submission file must be a PDF"
        ))
                .when(sendSubmissionUseCase)
                .execute(
                        eq("user@email.com"),
                        any(MultipartFile.class)
                );

        mockMvc.perform(
                        multipart("/api/submissions/send")
                                .file(file)
                                .principal(authentication)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Submission file must be a PDF"));
    }

    @Test
    void shouldReturnNotFoundWhenSubmissionDoesNotExist() throws Exception {

        UUID id = UUID.randomUUID();

        when(getSubmissionByIdUseCase.execute(id))
                .thenThrow(new SubmissionNotFoundByIdException(id));

        mockMvc.perform(
                        get("/api/submissions/id/{id}", id)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}