package com.ben.common.annotation;

import com.ben.common.validation.PhoneNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;
/**
 * PhoneNumber interface
 *
 * @author lomofu
 * @date 2020/01/15
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
  String message() default "手机号码格式不正确";

  Class[] groups() default {};

  Class[] payload() default {};
}
