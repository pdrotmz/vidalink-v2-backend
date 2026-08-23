package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetSubmissionsByIdUserUseCaseImpl implements GetSubmissionsByIdUserUseCase {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Submission> execute(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return submissionRepository.findByUserId(user.getId());
    }
}
