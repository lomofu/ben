package com.ben.company.exception;

import com.ben.common.exception.CustomException;
import com.ben.common.enums.ResultCode;
import com.ben.company.enums.CompanyEnum;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/1/24 00:13
 */
public class CompanyException extends CustomException {
  @Getter private final ResultCode resultCode;

  public CompanyException(CompanyEnum companyEnum) {
    super(companyEnum.getMessage());
    this.resultCode = companyEnum.getResultCode();
  }
}
