package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionFileFormatNotAcceptedException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotSentException;
import com.vidalink.healthcare.assessment.domain.model.Submission;
import com.vidalink.healthcare.assessment.domain.repository.SubmissionRepository;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.identity.domain.model.User;
import com.vidalink.healthcare.identity.domain.repository.UserRepository;
import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SendSubmissionUseCaseImpl implements SendSubmissionUseCase {

    private final SubmissionRepository repository;
    private final FileStorage fileStorage;
    private final UserRepository userRepository;

    @Override
    public SubmissionResponse execute(String email, MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (file == null || file.isEmpty()) {
            throw new SubmissionNotSentException("Submission file is required");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new SubmissionFileFormatNotAcceptedException("Submission file must be a PDF");
        }

        long maxBytes = DataSize.ofMegabytes(3).toBytes();

        if (file.getSize() > maxBytes) {
            throw new MaxUploadSizeExceededException(maxBytes);
        }

        Submission submission = new Submission();
        submission.setIdUser(user.getId());
        submission.setSentTime(LocalDateTime.now());
        submission.setStatus(ValidationStatus.PENDING);

        Submission savedSubmission = repository.save(submission);

        if (!file.isEmpty()) {

            String filePath = "submissions/" + savedSubmission.getId();
            try {
                String path = fileStorage.upload(
                        file.getInputStream(),
                        filePath,
                        file.getContentType()
                );

                submission.setFile(path);
                savedSubmission = repository.save(savedSubmission);
            } catch (IOException exception) {
                throw new SubmissionNotSentException("File could not upload: " + exception);
            }
        }
        return SubmissionResponse.from(savedSubmission);
    }
}
