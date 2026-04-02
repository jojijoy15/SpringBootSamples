package com.airportmanagement.domain.dto.validators;

import com.airportmanagement.domain.enums.FareConditions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FareConditionValidator implements ConstraintValidator<FareCondition, String> {

  @Override
  public void initialize(FareCondition constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String fc, ConstraintValidatorContext constraintValidatorContext) {
    log.info("fare condition is {}", fc);

    if (fc == null || fc.isBlank()) {
      return false;
    }

    String normalized = fc.trim().toLowerCase();
    return Arrays.stream(FareConditions.values())
      .map(value -> value.getValue().toLowerCase())
      .anyMatch(normalized::equals);
  }
}
