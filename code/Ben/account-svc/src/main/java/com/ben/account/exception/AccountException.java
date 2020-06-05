package com.ben.account.exception;

import com.ben.account.enums.AccountEnum;
import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;
import lombok.Getter;

public class AccountException extends CustomException {
  private static final long serialVersionUID = -5868855324628249318L;

  @Getter private final ResultCode resultCode;

  public AccountException(AccountEnum accountEnum) {
    super(accountEnum.getMessage());
    this.resultCode = accountEnum.getResultCode();
  }
}
