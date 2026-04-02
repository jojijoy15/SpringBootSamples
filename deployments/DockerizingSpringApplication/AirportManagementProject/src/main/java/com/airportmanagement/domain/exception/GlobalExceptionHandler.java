package com.airportmanagement.domain.exception;

import com.airportmanagement.domain.dto.RequestError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<List<RequestError>> handleRequestValidationError(ConstraintViolationException ex) {
    log.error("ConstraintViolationException", ex);
    List<RequestError> errors = ex.getConstraintViolations().stream()
      .map(this::mapConstraintViolation)
      .toList();
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<List<RequestError>> handleBodyValidationError(MethodArgumentNotValidException ex) {
    log.error("MethodArgumentNotValidException", ex);
    List<RequestError> errors = ex.getBindingResult().getFieldErrors().stream()
      .map(this::mapFieldError)
      .toList();
    return ResponseEntity.badRequest().body(errors);
  }

  private RequestError mapConstraintViolation(ConstraintViolation<?> violation) {
    String propertyPath = violation.getPropertyPath().toString();
    String[] pathParts = propertyPath.split("\\.");
    String errorCode = pathParts[pathParts.length - 1];
    return new RequestError(errorCode, violation.getMessage());
  }

  private RequestError mapFieldError(FieldError fieldError) {
    return new RequestError(fieldError.getField(), fieldError.getDefaultMessage());
  }

}
