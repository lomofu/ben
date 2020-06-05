package com.ben.sms.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/9 00:36
 */
public final class SmsConstant {
    public static final  String BEN_SMS_SVC = "ben-sms-svc";


  private SmsConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
