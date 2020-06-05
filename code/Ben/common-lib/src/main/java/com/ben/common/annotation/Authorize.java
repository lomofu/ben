package com.ben.common.annotation;

import java.lang.annotation.*;

/**
 * @author lomofu
 * @date 2020/2/29 22:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {
  // 允许调用的服务
  String[] value();
}
