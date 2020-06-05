package com.ben.company.exception;

import com.ben.common.exception.CustomException;
import com.ben.common.enums.ResultCode;
import com.ben.company.enums.JobEnum;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/2/14 15:13
 */
public class JobException extends CustomException {
  @Getter private final ResultCode resultCode;

  public JobException(JobEnum jobEnum) {
    super(jobEnum.getMessage());
    this.resultCode = jobEnum.getResultCode();
  }
}
