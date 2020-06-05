package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/1/29 02:01
 */
public class TeamConstant {
  public static final String SELECT_TEAM_ID = "teamId";

  private TeamConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
