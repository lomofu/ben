package com.ben.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author lomofu
 * @date 2020/2/29 20:18
 */
@Getter
@AllArgsConstructor
public enum AuthEnum {
  ILLEGAL_REQUEST(BAD_REQUEST.value(), "非法请求"),

  UNAUTHORIZED_TO_LOGIN(401, "没有权限,请先登录"),

  UNAUTHORIZED_EXPIRE(401, "认证过期,请重新登陆"),

  UNAUTHORIZED_FAIL(401, "认证失败,请重试");

  final int code;
  final String message;
}
