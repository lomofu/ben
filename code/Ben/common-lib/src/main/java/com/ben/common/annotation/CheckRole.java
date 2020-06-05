package com.ben.common.annotation;

import java.lang.annotation.*;

/**
 * @author lomofu
 * @date 2020/3/1 08:08
 *     <p>如果系统中不涉及其他角色可以用该注解代替 @checkPerm 注解,不过不建议（此注解仅通过角色进行校验）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface CheckRole {
  // 允许访问的角色
  String[] value();
}
