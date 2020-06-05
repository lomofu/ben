package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/1/29 02:02
 */
public class ProjectConstant {
  public static final String SELECT_PROJECT_ID = "projectId";

  private ProjectConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
