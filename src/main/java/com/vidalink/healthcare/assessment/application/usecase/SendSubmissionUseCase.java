package com.vidalink.healthcare.assessment.application.usecase;

import com.vidalink.healthcare.assessment.application.dto.SubmissionResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SendSubmissionUseCase {

    SubmissionResponse execute(String email, MultipartFile file);
}
