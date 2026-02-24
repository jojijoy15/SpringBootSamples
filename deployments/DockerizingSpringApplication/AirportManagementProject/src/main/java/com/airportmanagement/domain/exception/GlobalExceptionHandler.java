package com.airportmanagement.domain.exception;

import com.airportmanagement.domain.dto.RequestError;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<List<RequestError>> handleRequestValidationError(ConstraintViolationException ex) {
    List<RequestError> errors = ex.getConstraintViolations().stream()
        .map(e -> new RequestError(e.getPropertyPath().toString().split("\\.")[1], e.getMessage()))
        .toList();
    return ResponseEntity.badRequest().body(errors);
  }

}
