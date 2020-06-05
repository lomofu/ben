package com.ben.common.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/2/29 22:32
 */
public final class SecretConstant {

  public static final String DEFAULT_SECRET_TOKEN =
      "YzA0ODM3ZGY2MWYzNmJhNjg0NjYwOWFlNmI4MTQxZDg5NmQ5NmNiOGJkMjJiYzI1OTU3ZDBmY2ViMDYxNjY5NjIwOTkxYjQ5MDdkMzEzZWQ0NTY4MzRkY2YxNzJhZDA2NDMxNzEyMDc5MjUyOWExY2RkZjE4OGVjYTRlMDMzYWM=";

  public static final String FTP_SECRET = "YmVuazhz";

  private SecretConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
