package com.ben.company.aop;

import com.auth0.jwt.interfaces.Claim;
import com.ben.account.client.AccountClient;
import com.ben.account.dto.PermissionDto;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.HttpContextHelper;
import com.ben.common.utils.JwtHelper;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ben.common.constant.AuthConstant.*;
import static com.ben.common.enums.ResultCode.UN_AUTHORIZED;

/**
 * @author lomofu
 * @date 2020/3/25 23:53
 */
@Setter
@Getter
@Aspect
@Order(4)
@Component
public class CheckPermAspect {

  @Resource private AccountClient accountClient;

  @Before("@annotation(com.ben.common.annotation.CheckRole)")
  public void doBefore(JoinPoint point) {
    Signature signature = point.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    CheckPerm checkPerm = methodSignature.getMethod().getAnnotation(CheckPerm.class);
    // 注解中定义的权限值
    String[] value = checkPerm.value();

    String token = HttpContextHelper.getAuthHeader();

    try {
      Map<String, Claim> claimMap = JwtHelper.verifyToken(token);
      String role = claimMap.get(CLAIM_ROLE).asString();
      if (StringUtils.isEmpty(role)) {
        throw new CustomException(UN_AUTHORIZED);
      }
      // 如果不是管理员角色需要进行验证
      if (!ROLE_ADMIN.equals(role)) {
        String id = claimMap.get(CLAIM_ID).asString();
        List<PermissionDto> permissions =
            accountClient.getPermissionByAccountIdForFeign(AUTHORIZATION_SERVICE_COMPANY, id);

        Optional<PermissionDto> any = Optional.empty();
        for (String val : value) {
          any = permissions.stream().filter(e -> e.getPerm().equals(val)).findAny();
          if (any.isPresent()) {
            break;
          }
        }
        if (!any.isPresent()) {
          throw new CustomException(UN_AUTHORIZED);
        }
      }
    } catch (Exception e) {
      throw new CustomException(UN_AUTHORIZED);
    }
  }
}
