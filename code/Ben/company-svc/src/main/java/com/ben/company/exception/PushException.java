package com.ben.company.exception;

import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;
import com.ben.company.enums.PushEnum;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/3/22 02:35
 */
public class PushException extends CustomException {

  private static final long serialVersionUID = -8367853421724017409L;
  @Getter private final ResultCode resultCode;

  public PushException(PushEnum pushEnum) {
    super(pushEnum.getMessage());
    this.resultCode = pushEnum.getResultCode();
  }
}
