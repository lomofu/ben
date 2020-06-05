package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/1/29 02:05
 */
public final class CommonConstant {
  public static final String SERVICE_NAME = "company-service";
  public static final String SELECT_ID = "id";
  public static final String SELECT_NAME = "name";
  public static final String SELECT_ACTIVE = "active";
  public static final String SELECT_ACCOUNT_ID = "accountId";

  private CommonConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
