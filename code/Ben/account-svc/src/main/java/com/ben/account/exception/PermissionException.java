package com.ben.account.exception;

import com.ben.account.enums.PermissionEnum;
import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/3/15 17:49
 */
public class PermissionException extends CustomException {
  private static final long serialVersionUID = 755982439978165032L;

  @Getter private final ResultCode resultCode;

  public PermissionException(PermissionEnum permissionEnum) {
    super(permissionEnum.getMessage());
    this.resultCode = permissionEnum.getResultCode();
  }
}
