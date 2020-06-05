package com.ben.common.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/2/29 22:32
 */
public final class AuthConstant {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";

  public static final String HEADER_DETAILS = "application/json;charset=UTF-8";

  public static final String CLAIM_ID = "id";

  public static final String CLAIM_NAME = "name";

  public static final String CLAIM_ROLE = "role";

  public static final String CLAIM_EMAIL = "email";

  public static final String CLAIM_CHECK = "check";

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String AUTHORIZATION_SERVICE = "service";

  public static final String AUTHORIZATION_SERVICE_ACCOUNT = "account-svc";

  public static final String AUTHORIZATION_SERVICE_COMPANY = "company-svc";

  public static final String AUTHORIZATION_SERVICE_GATEWAY = "gateway";

  public static final String AUTHORIZATION_SERVICE_BOT = "bot-svc";

  public static final String ROLE_ADMIN = "admin";

  public static final String ROLE_EMPLOYEE = "employee";

  public static final String OTHER = "other";

  private AuthConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
