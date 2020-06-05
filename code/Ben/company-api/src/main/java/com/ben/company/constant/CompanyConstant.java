package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020-01-21 18:42
 */
public class CompanyConstant {
  public static final String SELECT_COMPANY_ID = "companyId";

  private CompanyConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
