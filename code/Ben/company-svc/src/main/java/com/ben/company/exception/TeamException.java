package com.ben.company.exception;

import com.ben.common.exception.CustomException;
import com.ben.common.enums.ResultCode;
import com.ben.company.enums.TeamEnum;
import lombok.Getter;

/**
 * @author lomofu
 * @date 2020/1/26 23:14
 */
public class TeamException extends CustomException {
  @Getter private final ResultCode resultCode;

  public TeamException(TeamEnum teamEnum) {
    super(teamEnum.getMessage());
    this.resultCode = teamEnum.getResultCode();
  }
}
