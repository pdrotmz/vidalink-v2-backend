package com.vidalink.healthcare.assessment.infrastructure.adapter;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.infrastructure.persistence.adapter.SubmissionRepositoryAdapter;
import com.vidalink.healthcare.assessment.infrastructure.persistence.jpa.JpaSubmissionRepository;
import com.vidalink.healthcare.marketplace.domain.model.redemption.Redemption;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionRepositoryAdapterTest {

    @Mock
    private JpaSubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionRepositoryAdapter adapter;

    @Test
    void shouldSaveSubmission() {

        Submission submission = new Submission();

        when(submissionRepository.save(submission)).thenReturn(submission);

        Submission result = adapter.save(submission);

        assertEquals(submission, result);

        verify(submissionRepository).save(submission);
    }

    @Test
    void shouldFindById() {

        UUID id = UUID.randomUUID();

        Optional<Submission> submission = Optional.of(new Submission());

        when(submissionRepository.findById(id)).thenReturn(submission);

        Optional<Submission> result = adapter.findById(id);

        assertEquals(submission, result);

        verify(submissionRepository).findById(id);
    }

    @Test
    void shouldFindAll() {

        List<Submission> submissions = List.of(new Submission(), new Submission());

        when(submissionRepository.findAll()).thenReturn(submissions);

        List<Submission> result = adapter.findAll();

        assertNotNull(result);
        assertEquals(submissions, result);
        assertEquals(2, result.size());

        verify(submissionRepository).findAll();
    }

    @Test
    void shouldFindByIdUser() {

        UUID id = UUID.randomUUID();

        List<Submission> submissions = List.of(new Submission(), new Submission());

        when(submissionRepository.findByIdUser(id)).thenReturn(submissions);

        List<Submission> result = adapter.findByIdUser(id);

        assertNotNull(result);
        assertEquals(submissions, result);
        assertEquals(2, result.size());

        verify(submissionRepository).findByIdUser(id);
    }

    @Test
    void shouldFindByStatus() {

        ValidationStatus status = ValidationStatus.PENDING;

        SubmissionResponse firstSubmission = mock(SubmissionResponse.class);
        SubmissionResponse secondSubmission = mock(SubmissionResponse.class);

        List<SubmissionResponse> submissions = List.of(firstSubmission, secondSubmission);

        when(submissionRepository.findByStatus(status)).thenReturn(submissions);

        List<SubmissionResponse> result = adapter.findByStatus(status);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(submissionRepository).findByStatus(status);
    }

    @Test
    void shouldDeleteById() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id);

        verify(submissionRepository).deleteById(id);
    }

}
