package com.ben.common.constant;

import static com.ben.common.constant.CommonConstant.CAN_NOT_INSTANCE_THIS_CLASS;

/**
 * @author lomofu
 * @date 2020/3/2 14:58
 */
public final class LogConstant {

  public static final String METHOD_ERROR_TEMPLATE =
      "\n -> 线程: {} \n -> 类名: {} \n -> 方法: {} \n ->当前行号 {} \n -> 错误: {} ";

  public static final String AOP_METHOD_ERROR_TEMPLATE =
          "\n -> 线程: {} \n -> 类名: {} \n -> 方法: {} \n -> 参数: {} \n -> 错误: {} ";

  public static final String HTTP_EXCEPTION_TEMPLATE =
      "\n -> 请求Ip: {} \n -> 请求端口: {} \n -> 服务器Ip: {} \n -> 服务器端口: {} \n -> 请求方法: {} \n -> 请求路径: {} \n -> 请求参数: {} \n -> 异常: {} ";

  private LogConstant() {
    throw new IllegalStateException(CAN_NOT_INSTANCE_THIS_CLASS);
  }
}
