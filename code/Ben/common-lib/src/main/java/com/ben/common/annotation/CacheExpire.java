package com.ben.common.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author lomofu
 * @date 2020/2/27 21:37
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheExpire {
  @AliasFor("expire")
  long value() default 60L;

  @AliasFor("value")
  long expire() default 60L;
}
