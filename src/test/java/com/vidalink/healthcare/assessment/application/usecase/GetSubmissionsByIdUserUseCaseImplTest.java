package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSubmissionsByIdUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @InjectMocks
    private GetSubmissionsByIdUserUseCaseImpl useCase;

    @Test
    void shouldReturnSubmissionsByUserId() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Submission submission1 = new Submission();
        Submission submission2 = new Submission();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(submissionRepository.findByUserId(userId))
                .thenReturn(List.of(submission1, submission2));

        List<Submission> submissions =
                useCase.execute(userId);

        assertEquals(2, submissions.size());

        verify(userRepository).findById(userId);
        verify(submissionRepository).findByUserId(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> useCase.execute(userId)
        );

        verify(userRepository).findById(userId);
        verifyNoInteractions(submissionRepository);
    }
}
