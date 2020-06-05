package com.ben.common.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/26 19:36
 */
public final class PermissionConstant {

  public static final String SYS_ACCOUNT_VIEW = "sys:account:view";

  public static final String SYS_ACCOUNT_EDIT = "sys:account:edit";

  public static final String SYS_EMPLOYEE_ADD = "sys:employee:add";

  public static final String SYS_EMPLOYEE_DEL = "sys:employee:del";

  public static final String SYS_EMPLOYEE_EDIT = "sys:employee:edit";

  public static final String SYS_EMPLOYEE_VIEW = "sys:employee:view";

  public static final String SYS_PERMISSION_VIEW = "sys:permission:view";

  public static final String SYS_ROLE_ADD = "sys:role:add";

  public static final String SYS_ROLE_EDIT = "sys:role:edit";

  public static final String SYS_ROLE_DEL = "sys:role:del";

  public static final String SYS_ROLE_PERMISSION_EDIT = "sys:role:permission:edit";

  public static final String SYS_COMPANY_EDIT = "sys:company:edit";

  public static final String SYS_TEAM_VIEW = "sys:team:view";

  public static final String SYS_PROJECT_VIEW = "sys:project:view";

  public static final String SYS_TEAM_ADD = "sys:team:add";

  public static final String SYS_TEAM_EDIT = "sys:team:edit";

  public static final String SYS_TEAM_DEL = "sys:team:del";

  public static final String SYS_PROJECT_ADD = "sys:project:add";

  public static final String SYS_PROJECT_EDIT = "sys:project:edit";

  public static final String SYS_PROJECT_DEL = "sys:project:del";

  public static final String SYS_PROJECT_CHANGE = "sys:project:change";

  public static final String SYS_JOB_VIEW = "sys:job:view";

  public static final String SYS_JOB_ADD = "sys:job:add";

  public static final String SYS_JOB_EDIT = "sys:job:edit";

  public static final String SYS_JOB_DEL = "sys:job:del";

  public static final String SYS_MESSAGE_SEND = "sys:message:send";

  public static final String SYS_JOB_SEND = "sys:job:send";

  private PermissionConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
