package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.exception.SubmissionsNotFoundByIdUserException;
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

import static org.junit.jupiter.api.Assertions.*;
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
        List<Submission> submissionsList = List.of(submission1, submission2);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(submissionRepository.findByIdUser(userId))
                .thenReturn(submissionsList);

        List<Submission> submissions = useCase.execute(userId);

        assertNotNull(submissions);
        assertEquals(2, submissions.size());

        verify(userRepository).findById(userId);
        verify(submissionRepository, times(3)).findByIdUser(userId);
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

    @Test
    void shouldThrowSubmissionNotFoundByIdExceptionWhenSubmissionsDoNotExist() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(submissionRepository.findByIdUser(userId))
                .thenReturn(List.of());

        assertThrows(
                SubmissionsNotFoundByIdUserException.class,
                () -> useCase.execute(userId)
        );

        verify(userRepository).findById(userId);
        verify(submissionRepository, times(2)).findByIdUser(userId);
    }

}
