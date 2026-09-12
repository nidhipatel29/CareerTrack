package com.CareerTrack.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<?> handleException(MethodArgumentNotValidException exception) {

      Map<String, String> errors = new HashMap<>();

      // store the field errors
      List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

      for (FieldError fieldError : fieldErrors) {
         errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      }
      return ResponseEntity.badRequest().body(errors);

   }

   @ExceptionHandler(CompanyNotFoundException.class)
   public ResponseEntity<String> handleCompanyNotFoundException(CompanyNotFoundException exception) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
   }

   @ExceptionHandler(JobNotFoundException.class)
   public ResponseEntity<String> handleJobNotFoundException(JobNotFoundException exception) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
   }

   @ExceptionHandler(ApplicationNotFoundException.class)
   public ResponseEntity<String> handleApplicationNotFoundException(ApplicationNotFoundException exception) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
   }

   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
   }

   @ExceptionHandler(DuplicateApplicationException.class)
   public ResponseEntity<String> handleDuplicateApplicationException(DuplicateApplicationException exception) {

      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
   }

   @ExceptionHandler(InvalidRequestException.class)
   public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException exception) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
   }

   @ExceptionHandler(EmailAlreadyExistsException.class)
   public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {

      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
   }

   @ExceptionHandler(BadCredentialsException.class)
   public ResponseEntity<String> handleBadCredentials(
         BadCredentialsException exception) {

      return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Invalid email or password");
   }



 @ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<String> handleAccessDeniedException(
        AccessDeniedException exception) {

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(exception.getMessage());
}
}
