package com.vidalink.healthcare.shared.presentation;

import com.vidalink.healthcare.assessment.domain.exception.SubmissionFileFormatNotAcceptedException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotFoundByIdException;
import com.vidalink.healthcare.assessment.domain.exception.SubmissionNotSentException;
import com.vidalink.healthcare.identity.application.dto.response.ErrorResponse;
import com.vidalink.healthcare.identity.domain.exception.CpfAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.exception.EmailAlreadyExistsException;
import com.vidalink.healthcare.identity.domain.exception.InvalidCredentialsException;
import com.vidalink.healthcare.identity.domain.exception.UserNotFoundException;
import com.vidalink.healthcare.marketplace.domain.exception.redemption.*;
import com.vidalink.healthcare.marketplace.domain.exception.reward.ImageEmptyException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardAlreadyExistsByNameException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByIdException;
import com.vidalink.healthcare.marketplace.domain.exception.reward.RewardNotFoundByNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    // ENTITY
    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyExistsException(CpfAlreadyExistsException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialException(InvalidCredentialsException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    // MARKETPLACE
    @ExceptionHandler(RewardAlreadyExistsByNameException.class)
    public ResponseEntity<ErrorResponse> handleRewardAlreadyExistsBYNameException(RewardAlreadyExistsByNameException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RewardNotFoundByIdException.class)
    public ResponseEntity<ErrorResponse> handleRewardNotFoundByIdException(RewardNotFoundByIdException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RewardNotFoundByNameException.class)
    public ResponseEntity<ErrorResponse> handleRewardNotFoundByNameException(RewardNotFoundByNameException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RewardInsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleRewardInsufficientStockException(RewardInsufficientStockException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ImageEmptyException.class)
    public ResponseEntity<ErrorResponse> handleImageEmptyException(ImageEmptyException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //ASSESSMENT
    @ExceptionHandler(SubmissionNotSentException.class)
    public ResponseEntity<ErrorResponse> handleSubmissionNotSentException(SubmissionNotSentException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SubmissionFileFormatNotAcceptedException.class)
    public ResponseEntity<ErrorResponse> handleSubmissionFileFormatNotAcceptedException(SubmissionFileFormatNotAcceptedException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SubmissionNotFoundByIdException.class)
    public ResponseEntity<ErrorResponse> handleSubmissionNotFoundByIdException(SubmissionNotFoundByIdException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // REDEMPTION
    @ExceptionHandler(RedemptionAmountUnderThanZeroException.class)
    public ResponseEntity<ErrorResponse> handleRedemptionAmountUnderThanZeroException(RedemptionAmountUnderThanZeroException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RedemptionNotFoundByIdException.class)
    public ResponseEntity<ErrorResponse> handleRedemptionNotFoundByIdException(RedemptionNotFoundByIdException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RedemptionNotFoundByIdRewardException.class)
    public ResponseEntity<ErrorResponse> handleRedemptionNotFoundByIdRewardException(RedemptionNotFoundByIdRewardException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RedemptionNotFoundByIdUserException.class)
    public ResponseEntity<ErrorResponse> handleRedemptionNotFoundByIdUserException(RedemptionNotFoundByIdUserException exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
