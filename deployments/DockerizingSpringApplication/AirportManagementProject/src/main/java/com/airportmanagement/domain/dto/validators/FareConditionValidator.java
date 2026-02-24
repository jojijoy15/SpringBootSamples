package com.airportmanagement.domain.dto.validators;

import com.airportmanagement.domain.enums.FareConditions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FareConditionValidator implements ConstraintValidator<FareCondition, FareConditions> {

  @Override
  public void initialize(FareCondition constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(FareConditions s, ConstraintValidatorContext constraintValidatorContext) {
    log.info("fare condition is {}", s);
    return Arrays.stream(FareConditions.values())
        .anyMatch(fareConditions -> fareConditions.equals(s) );
  }
}
