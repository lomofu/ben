package com.ben.bot.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/8 20:16
 */
public final class SmsConstant {

  public static final String SEND_SMS_WITH_LOGIN_WITH_PHONE_CODE = "sendSMSWithLoginWithPhoneCode";

  public static final String SEND_SMS_WITH_LOGIN_TO_MUCH = "sendSMSWithLoginToMuch";

  public static final String SEND_SMS_WITH_NOTIFICATION = "sendSMSWithNotification";

  public static final String SEND_SMS_WITH_SCHEDULE = "sendSMSWithSchedule";

  private SmsConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
