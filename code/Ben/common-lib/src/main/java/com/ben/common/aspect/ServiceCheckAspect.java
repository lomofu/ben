package com.ben.common.aspect;

import com.ben.common.annotation.Authorize;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.HttpContextHelper;
import com.ben.common.utils.RedisHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.enums.ResultCode.UN_AUTHORIZED;

/**
 * @author lomofu
 * @date 2020/3/3 20:46
 */
@Aspect
@Component
@Slf4j
@Setter
@Getter
@Order(3)
public class ServiceCheckAspect {
  @Resource private RedisHelper redisHelper;

  @Value("${app.env}")
  private String env;

  @Pointcut("@annotation(com.ben.common.annotation.Authorize)")
  public void service() {
    // Do nothing because of pointcut
  }

  @Before("service()")
  public void doBefore(JoinPoint point) {
    if (!getEnv().equalsIgnoreCase("local")) {
      Signature signature = point.getSignature();
      MethodSignature methodSignature = (MethodSignature) signature;
      Authorize authorize = methodSignature.getMethod().getAnnotation(Authorize.class);
      List<String> list = Arrays.asList(authorize.value());
      String service =
          Optional.ofNullable(HttpContextHelper.getServiceHeader())
              .orElseThrow(() -> new CustomException(UN_AUTHORIZED));
      String fromHeader = HttpContextHelper.getFromHeader();

      // 如果来自网关查询请求头中的id是否在缓存中包含
      if (Objects.nonNull(fromHeader)
          && list.contains(AUTHORIZATION_SERVICE_GATEWAY)
          && !redisHelper.hasKey("gateway::" + service)) {
        throw new CustomException(UN_AUTHORIZED);
      }
      // 如果通过内部服务间调用无需比对网关id
      if (Objects.isNull(fromHeader) && !list.contains(service)) {
        throw new CustomException(UN_AUTHORIZED);
      }
    }
  }
}
