package com.ben.common.constant;

public final class CommonConstant {

  // 缓存失效1小时
  public static final long DEFAULT_CACHE_TIME = 3600L;

  public static final String CAN_NOT_INSTANCE_THIS_CLASS = "该类不可以被实例化";

  public static final String UTILITY_CLASS = "此为工具类";

  public static final String ILLEGAL_EMAIL = "非法邮箱地址参数";

  public static final String PARAM_MISSING = "请求参数丢失或者为空";

  public static final String EMAIL_REGX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

  public static final String PHONE_REGX =
      "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

  private CommonConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
