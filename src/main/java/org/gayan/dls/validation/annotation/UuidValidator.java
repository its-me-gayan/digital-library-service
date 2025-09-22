package org.gayan.dls.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

/** Author: Gayan Sanjeewa User: gayan Date: 9/21/25 Time: 12:50 AM */
public class UuidValidator implements ConstraintValidator<ValidUUID, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      UUID.fromString(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
