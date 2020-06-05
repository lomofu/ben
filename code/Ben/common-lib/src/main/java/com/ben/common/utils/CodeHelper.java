package com.ben.common.utils;

import java.util.Random;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;

/**
 * @author lomofu
 * @date 2020/3/9 00:57
 */
public class CodeHelper {

  public static final String ILLEGAL_ARG = "输入位数不能小于1";

  public static final Random random = new Random();

  private CodeHelper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static String generateRandomNumber(int num) {
    if (num < 1) {
      throw new IllegalArgumentException(ILLEGAL_ARG);
    }
    StringBuffer str = new StringBuffer();
    for (int i = 0; i < num; i++) {
      str.append(random.nextInt(10));
    }
    return str.toString();
  }
}
