package com.ben.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Result Code Enum
 *
 * @author lomofu
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
  SUCCESS(OK.value(), "请求成功"),

  FAILURE(BAD_REQUEST.value(), "请求失败"),

  UN_AUTHORIZED(UNAUTHORIZED.value(), "没有权限"),

  NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404 页面不存在"),

  MSG_NOT_READABLE(BAD_REQUEST.value(), "消息不可读"),

  METHOD_NOT_SUPPORTED(METHOD_NOT_ALLOWED.value(), "方法不支持"),

  REQ_REJECT(FORBIDDEN.value(), "请求拒绝"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器错误"),

  PARAM_MISS(BAD_REQUEST.value(), "缺少请求参数"),

  PARAM_TYPE_ERROR(BAD_REQUEST.value(), "参数不匹配"),

  PARAM_BIND_ERROR(BAD_REQUEST.value(), "参数绑定错误"),

  PARAM_VALID_ERROR(BAD_REQUEST.value(), "参数校验错误"),

  TIME_OUT(BAD_REQUEST.value(), "网络忙");

  final int code;

  final String message;
}
