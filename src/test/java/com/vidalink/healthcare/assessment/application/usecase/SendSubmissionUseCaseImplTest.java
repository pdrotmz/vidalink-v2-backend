package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionFileFormatNotAcceptedException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotSentException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendSubmissionUseCaseImplTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private SendSubmissionUseCaseImpl useCase;

    @Test
    void shouldSendSubmissionSuccessfully() throws IOException {
        String email = "user@email.com";
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(DataSize.ofMegabytes(1).toBytes());
        when(file.getInputStream())
                .thenReturn(new ByteArrayInputStream("test".getBytes()));

        when(fileStorage.upload(
                any(InputStream.class),
                anyString(),
                eq("application/pdf")
        )).thenReturn("submissions/test-file.pdf");

        when(submissionRepository.save(any(Submission.class)))
                .thenAnswer(invocation -> {
                    Submission submission = invocation.getArgument(0);

                    if (submission.getId() == null) {
                        submission.setId(UUID.randomUUID());
                    }

                    return submission;
                });

        SubmissionResponse response = useCase.execute(email, file);

        assertNotNull(response);

        verify(userRepository).findByEmail(email);
        verify(fileStorage).upload(
                any(InputStream.class),
                anyString(),
                eq("application/pdf")
        );
        verify(submissionRepository, times(2)).save(any(Submission.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        String email = "user@email.com";
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(email, file)
        );

        verify(submissionRepository, never()).save(any());
        verifyNoInteractions(fileStorage);
    }

    @Test
    void shouldThrowExceptionWhenFileIsNotPdf() {
        // Arrange
        String email = "user@email.com";

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        // Act + Assert
        assertThrows(
                SubmissionFileFormatNotAcceptedException.class,
                () -> useCase.execute(email, file)
        );

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(fileStorage);
    }

    @Test
    void shouldThrowExceptionWhenFileExceedsMaxSize() {
        // Arrange
        String email = "user@email.com";

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize())
                .thenReturn(DataSize.ofMegabytes(4).toBytes());

        // Act + Assert
        assertThrows(
                MaxUploadSizeExceededException.class,
                () -> useCase.execute(email, file)
        );

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(fileStorage);
    }

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        // Arrange
        String email = "user@email.com";

        User user = new User();
        user.setId(UUID.randomUUID());

        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(file.isEmpty()).thenReturn(true);

        // Act + Assert
        assertThrows(
                SubmissionNotSentException.class,
                () -> useCase.execute(email, file)
        );

        verify(userRepository).findByEmail(email);
        verify(submissionRepository, never()).save(any(Submission.class));
        verifyNoInteractions(fileStorage);
    }

    @Test
    void shouldThrowSubmissionNotSentExceptionWhenFileInputStreamFails() throws IOException {
        String email = "test@email.com";
        UUID userId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Submission savedSubmission = new Submission();
        savedSubmission.setId(submissionId);

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");

        long validSize = DataSize.ofMegabytes(1).toBytes();
        when(file.getSize()).thenReturn(validSize);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(submissionRepository.save(any(Submission.class))).thenReturn(savedSubmission);

        IOException simulatedException = new IOException("Simulated disk error");
        when(file.getInputStream()).thenThrow(simulatedException);

        SubmissionNotSentException exception = assertThrows(SubmissionNotSentException.class, () -> {
            useCase.execute(email, file);
        });

        String expectedMessage = "File could not upload: " + simulatedException;
        assertEquals(expectedMessage, exception.getMessage());

        verify(userRepository).findByEmail(email);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

}
