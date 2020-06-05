package com.ben.company.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/13 23:24
 */
public final class SearchConstant {
  public static final String START = "start";
  public static final String ACTIVE = "active";
  public static final String DESCRIPTION = "description";
  public static final String NAME = "name";
  public static final String ID = "id";
  public static final String CREATE_TIME = "create_time";
  public static final String COMPANY_ID = "company_id";
  public static final String ACCOUNT_ID = "account_id";
  public static final String PROJECT_ID = "project_id";
  public static final String SORT_DEFAULT_VALUE = "create_time";
  public static final String ASTERISK = "*";
  public static final String TEAM_MONTH_CHART_DATA = "team_month_chart_data";
  public static final String PROJECT_MONTH_CHART_DATA = "project_month_chart_data";
  public static final String JOB_WEEK_LINE_DATA = "job_week_line_data";
  public static final String MONTH_FORMAT = "yyyy-MM";
  public static final String DAY_FORMAT = "yyyy-MM-dd";
  public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

  private SearchConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
