package com.ben.account.client;

import com.ben.account.constant.AccountConstant;
import com.ben.account.dto.AccountDto;
import com.ben.account.dto.PermissionDto;
import com.ben.account.vo.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

/**
 * AccountClient interface 用于java内部rest服务提供者
 *
 * @author lomofu
 * @date 2020/01/15
 */
@FeignClient(
    name = AccountConstant.SERVICE_NAME,
    path = "/api/accounts",
    url = "${app.endpoint.account-service-endpoint}")
public interface AccountClient {

  /**
   * feign服务提供者: 根据账户ID获取账户信息(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param token token
   * @param id 账户id
   * @return AccountResponse
   */
  @GetMapping(path = "/feign/id")
  AccountResponse getAccountByIdForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id);

  /**
   * feign服务提供者: 根据账户ID获取账户信息(用于服务间远程调用)
   *
   * @param id 账户id
   * @return AccountDto
   */
  @GetMapping(path = "/feign/{id}")
  AccountDto getAccountByIdForFeign(@PathVariable @NotBlank String id);

  /**
   * feign服务提供者: 根据账户ID数组查询一组账户信息(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param list 一组账户id数组
   * @return List<AccountDto>
   */
  @GetMapping(path = "/feign/list")
  List<AccountDto> getAccountByListIdForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service, @RequestParam String[] list);

  /**
   * feign服务提供者: 根据账户ID数组查询该账户的权限集(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param id 账户id
   * @return List<PermissionDto>
   */
  @GetMapping(path = "/feign/perm")
  List<PermissionDto> getPermissionByAccountIdForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service, @RequestParam String id);

  /**
   * feign服务提供者: 批量删除团队中未激活员工账号(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param list 一组未激活员工id列表
   * @return AccountResponse
   */
  @DeleteMapping(path = "/feign/teamMapping")
  AccountResponse deleteTempEmployeeMappingAccount(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody @NotEmpty List<String> list);
}
