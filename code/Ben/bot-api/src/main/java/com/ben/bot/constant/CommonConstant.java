package com.ben.bot.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/4 05:05
 */
public final class CommonConstant {

  public static final String SERVICE_MAIL_NAME = "bot-mail-service";

  public static final String SERVICE_SMS_NAME = "bot-sms-service";

  public static final String SERVICE_APP_NAME = "bot-app-service";

  private CommonConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
