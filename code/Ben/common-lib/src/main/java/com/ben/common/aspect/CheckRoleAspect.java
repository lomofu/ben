package com.ben.common.aspect;

import com.auth0.jwt.interfaces.Claim;
import com.ben.common.annotation.CheckRole;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.HttpContextHelper;
import com.ben.common.utils.JwtHelper;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

import static com.ben.common.constant.AuthConstant.CLAIM_ROLE;
import static com.ben.common.enums.ResultCode.UN_AUTHORIZED;

/**
 * @author lomofu
 * @date 2020/3/1 08:09
 */
@Setter
@Getter
@Aspect
@Order(5)
@Component
public class CheckRoleAspect {
  @Value("${app.env}")
  private String env;

  @Around("@annotation(com.ben.common.annotation.CheckRole)")
  public Object checkRole(ProceedingJoinPoint point) throws Throwable {
    if (getEnv().equalsIgnoreCase("local")) {
      return point.proceed();
    } else {
      String token = HttpContextHelper.getAuthHeader();
      Signature signature = point.getSignature();
      MethodSignature methodSignature = (MethodSignature) signature; // 获取参数名
      CheckRole checkRole = methodSignature.getMethod().getAnnotation(CheckRole.class);
      String[] allows = checkRole.value();

      try {
        Map<String, Claim> claimMap = JwtHelper.verifyToken(token);
        String role = claimMap.get(CLAIM_ROLE).asString();
        if (StringUtils.isEmpty(role) || !Arrays.asList(allows).contains(role)) {
          throw new CustomException(UN_AUTHORIZED);
        }
        return point.proceed();
      } catch (Exception e) {
        throw new CustomException(UN_AUTHORIZED);
      }
    }
  }
}
