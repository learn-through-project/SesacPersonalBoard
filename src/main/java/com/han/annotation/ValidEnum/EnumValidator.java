package com.han.annotation.ValidEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
  private Set<String> validValues;

  @Override
  public void initialize(ValidEnum annotation) {
    validValues = Arrays.stream(annotation.enumClass().getEnumConstants())
            .map((e) -> e.name().toLowerCase())
            .collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    return value != null && validValues.contains(value.name().toLowerCase());
  }
}
