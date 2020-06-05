package com.ben.common.exception;

import com.ben.common.response.BaseResponse;
import com.ben.common.utils.HttpContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.ben.common.constant.LogConstant.HTTP_EXCEPTION_TEMPLATE;
import static com.ben.common.enums.ResultCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public BaseResponse handleError(CustomException e) {
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        e.getMessage());
    return BaseResponse.builder().code(e.getResultCode().getCode()).msg(e.getMessage()).build();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public BaseResponse handleError(MissingServletRequestParameterException e) {
    String message = String.format("%s: %s", PARAM_MISS.getMessage(), e.getParameterName());
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        message);
    return BaseResponse.builder().code(PARAM_MISS.getCode()).msg(message).build();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public BaseResponse handleError(MethodArgumentTypeMismatchException e) {
    String message = String.format("%s: %s", PARAM_TYPE_ERROR.getMessage(), e.getName());
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        message);
    return BaseResponse.builder().code(PARAM_TYPE_ERROR.getCode()).msg(message).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public BaseResponse handleError(MethodArgumentNotValidException e) {
    BindingResult result = e.getBindingResult();
    FieldError error = result.getFieldError();
    assert error != null;
    String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        message);
    return BaseResponse.builder().code(PARAM_VALID_ERROR.getCode()).msg(message).build();
  }

  @ExceptionHandler(AsyncRequestTimeoutException.class)
  public BaseResponse handleError(AsyncRequestTimeoutException e) {
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        e.getMessage());
    return BaseResponse.builder().code(TIME_OUT.getCode()).msg(TIME_OUT.getMessage()).build();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public BaseResponse handleError(HttpMessageNotReadableException e) {
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        e.getMessage());
    return BaseResponse.builder()
        .code(PARAM_TYPE_ERROR.getCode())
        .msg(PARAM_TYPE_ERROR.getMessage())
        .build();
  }

  @ExceptionHandler(Throwable.class)
  public BaseResponse handleError(Throwable e) {
    log.error(
        HTTP_EXCEPTION_TEMPLATE,
        HttpContextHelper.getRemoteAddr(),
        HttpContextHelper.getRemotePort(),
        HttpContextHelper.getLocalAddr(),
        HttpContextHelper.getLocalPort(),
        HttpContextHelper.getMethod(),
        HttpContextHelper.getRequestPath(),
        HttpContextHelper.getParameter(),
        e.getMessage());
    return BaseResponse.builder()
        .code(INTERNAL_SERVER_ERROR.getCode())
        .msg(INTERNAL_SERVER_ERROR.getMessage())
        .build();
  }
}
