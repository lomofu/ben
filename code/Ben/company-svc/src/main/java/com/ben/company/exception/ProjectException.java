package com.ben.company.exception;

import com.ben.common.exception.CustomException;
import com.ben.common.enums.ResultCode;
import com.ben.company.enums.ProjectEnum;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/1/27 20:13
 */
public class ProjectException extends CustomException {
  @Getter private final ResultCode resultCode;

  public ProjectException(ProjectEnum projectEnum) {
    super(projectEnum.getMessage());
    this.resultCode = projectEnum.getResultCode();
  }
}
