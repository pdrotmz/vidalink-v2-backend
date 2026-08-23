package com.vidalink.healthcare.assessment.application.dto;

import org.springframework.web.multipart.MultipartFile;

public record SubmissionRequest(
        String email,
        MultipartFile file
) {
}
