package com.ben.account.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

public final class AccountConstant {
  public static final String SERVICE_NAME = "account-svc";

  public static final String SORT_DEFAULT_VALUE = "is_admin";

  private AccountConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
