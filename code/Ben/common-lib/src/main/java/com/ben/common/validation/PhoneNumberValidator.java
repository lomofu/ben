package com.ben.common.validation;

import com.ben.common.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
/**
 * PhoneNumberValidator class
 *
 * @author lomofu
 * @date 2020/01/15
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  @Override
  public boolean isValid(
      String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
    return Objects.nonNull(phoneNumber) && phoneNumber.matches("^1(3|4|5|7|8)\\d{9}$");
  }
}
