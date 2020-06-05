package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/2/14 16:14
 */
public class JobConstant {

  public static final String SELECT_JOB_ID = "jobId";

  public static final String SELECT_COLOR = "color";

  public static final String SELECT_START = "start";

  public static final String SELECT_END = "end";

  private JobConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
