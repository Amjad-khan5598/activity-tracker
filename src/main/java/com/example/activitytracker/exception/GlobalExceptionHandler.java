package com.example.activitytracker.exception;

import java.util.HashMap;
import java.util.Map;

import com.example.activitytracker.DTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(
            InvalidCredentialsException ex) {

        return buildError(HttpStatus.UNAUTHORIZED,
                "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        return buildError(HttpStatus.CONFLICT,
                "EMAIL_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleTaskNotFoundException(
            TaskNotFoundException ex) {

        return buildError(HttpStatus.NOT_FOUND,
                "TASK_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(
            UserNotFoundException ex) {

        return buildError(HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(TaskAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleTaskAccessDeniedException(
            TaskAccessDeniedException ex) {

        return buildError(HttpStatus.FORBIDDEN,
                "TASK_ACCESS_DENIED", ex.getMessage());
    }


@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));

    ErrorResponseDTO response = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_FAILED",
            "One or more fields are invalid.",
            errors
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
}


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            RuntimeException ex) {

        // Log the exception internally in a later step.
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred.");
    }

    private ResponseEntity<ErrorResponseDTO> buildError(
            HttpStatus status, String error, String message) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                status.value(), error, message);

        return ResponseEntity.status(status).body(response);
    }
}
