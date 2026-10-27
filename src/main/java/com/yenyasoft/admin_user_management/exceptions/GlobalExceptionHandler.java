package com.yenyasoft.admin_user_management.exceptions;

import com.yenyasoft.admin_user_management.dtos.GlobalResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: GlobalExceptionHandler.java
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //handle already exists
    @ExceptionHandler(value = AlreadyExistsException.class)
    public GlobalResponse<Object> handleAlreadyExistsException(AlreadyExistsException e) {
        return GlobalResponse.error(
                e.getMessage(),
                null
        );
    }
    // to validate the request
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull GlobalResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .message(message)
                .success(false)
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);

    }
    // to explicitly handle the database exceptions
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public GlobalResponse<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        String error = ex.getMostSpecificCause().getMessage();

        if (error != null) {
            if(error.contains("uk_")){
                message = "This violates the unique key constraint";
            } else if (error.contains("fk_")) {
                message = "Cannot perform this action because related data exists or is missing";
            }
            else if(error.contains("not-null constraint")) {
                message = "A required field is missing";
            }

        }
        return GlobalResponse.error(message, null);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public GlobalResponse<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return GlobalResponse.error(
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<GlobalResponse<Void>> handleDisabledException(
            DisabledException ex) {

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .message("Your account is pending admin approval. You cannot log in yet.")
                .success(false)
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GlobalResponse<Void>> handleResponseStatusException(
            ResponseStatusException exception) {

        GlobalResponse<Void> response = GlobalResponse.error(
                exception.getReason() != null
                        ? exception.getReason()
                        : "Request failed",
                null
        );

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalResponse<Void>> handleAuthenticationException(
            AuthenticationException ex) {

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .message("Invalid username or password.")
                .success(false)
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

}
