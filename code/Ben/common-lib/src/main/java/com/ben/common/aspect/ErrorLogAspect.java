package com.ben.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.ben.common.constant.LogConstant.AOP_METHOD_ERROR_TEMPLATE;

/**
 * @author lomofu
 * @date 2020/3/2 15:25
 */
@Aspect
@Component
@Slf4j
@Order(6)
public class ErrorLogAspect {

  @Pointcut("execution(public * com.ben.*.service.*.*.*(..))")
  public void errorLog() {}

  @AfterThrowing(pointcut = "errorLog()", throwing = "e")
  public void doAfterThrowing(JoinPoint point, Exception e) {
    String className = point.getTarget().getClass().getName();
    String methodName = point.getSignature().getName();
    List<Object> args = Arrays.asList(point.getArgs());
    log.error(
        AOP_METHOD_ERROR_TEMPLATE,
        Thread.currentThread().getName(),
        className,
        methodName,
        args,
        e.getMessage());
  }
}
