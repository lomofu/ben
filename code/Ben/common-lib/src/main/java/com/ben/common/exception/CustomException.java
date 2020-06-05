package com.ben.common.exception;

import com.ben.common.enums.ResultCode;
import lombok.Getter;

public class CustomException extends RuntimeException {

  private static final long serialVersionUID = -4368598366034243481L;

  @Getter private final ResultCode resultCode;

  public CustomException(String message) {
    super(message);
    this.resultCode = ResultCode.FAILURE;
  }

  public CustomException(ResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }

  public CustomException(String message, ResultCode resultCode) {
    super(message);
    this.resultCode = resultCode;
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
    this.resultCode = ResultCode.FAILURE;
  }

  @Override
  public Throwable fillInStackTrace() {
    return this;
  }

  public Throwable doFillInStackTrace() {
    return super.fillInStackTrace();
  }
}
