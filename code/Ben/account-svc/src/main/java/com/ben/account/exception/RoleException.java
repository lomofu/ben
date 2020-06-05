package com.ben.account.exception;

import com.ben.account.enums.RoleEnum;
import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/3/15 17:25
 */
public class RoleException extends CustomException {
  private static final long serialVersionUID = -3382030532600295949L;

  @Getter private final ResultCode resultCode;

  public RoleException(RoleEnum roleEnum) {
    super(roleEnum.getMessage());
    this.resultCode = roleEnum.getResultCode();
  }
}
