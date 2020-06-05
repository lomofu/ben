package com.ben.common.annotation;

import java.lang.annotation.*;

/**
 * @author lomofu
 * @date 2020/3/25 23:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPerm {
  // 允许访问的权限
  String[] value();
}
