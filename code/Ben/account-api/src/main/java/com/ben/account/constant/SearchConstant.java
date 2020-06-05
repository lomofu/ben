package com.ben.account.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/13 14:34
 */
public final class SearchConstant {
  public static final String COMPANY_ID = "company_id";
  public static final String TEAM_ID = "team_id";
  public static final String PROJECT_ID = "project_id";
  public static final String PHONE_NUMBER = "phone_number";
  public static final String IS_ACTIVE = "is_active";
  public static final String NAME = "name";
  public static final String EMAIL = "email";
  public static final String ID = "id";
  public static final String ASTERISK = "*";

  private SearchConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
