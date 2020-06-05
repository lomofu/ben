package com.ben.company.client;

import com.ben.company.dto.company.NewCompanyDto;
import com.ben.company.vo.company.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;
import static com.ben.company.constant.CommonConstant.SERVICE_NAME;

/**
 * CompanyClient interface 用于java内部rest服务提供者
 *
 * @author lomofu
 * @date 2020-01-21 18:41
 */
@FeignClient(
    name = SERVICE_NAME,
    path = "/api/companies",
    url = "${app.endpoint.company-service-endpoint}")
public interface CompanyClient {

  /**
   * feign服务提供者: 用于创建新公司或团队(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param newCompanyDto 包含账户id,公司名称,公司地址
   * @return CompanyResponse
   */
  @PostMapping(path = "/feign/company")
  CompanyResponse createNewCompanyForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody NewCompanyDto newCompanyDto);

  /**
   * feign服务提供者: 用于创建新公司或团队(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param token token
   * @param id 公司id
   * @return CompanyResponse
   */
  @GetMapping(path = "/feign/company")
  CompanyResponse getCompanyByIdForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id);

  /**
   * feign服务提供者: 创建员工与公司的映射关系,(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param accountId 账户id
   * @param companyId 公司id
   * @return CompanyResponse
   */
  @GetMapping(path = "/feign/employeeWithCompany")
  CompanyResponse createEmployeeMappingWithCompanyForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestParam("accountId") @NotBlank(message = PARAM_MISSING) String accountId,
      @RequestParam("companyId") @NotBlank(message = PARAM_MISSING) String companyId);

  /**
   * feign服务提供者: 创建员工与团队的映射关系,(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param accountId 账户id
   * @param teamId 团队id
   * @return CompanyResponse
   */
  @GetMapping(path = "/feign/employeeWithTeam")
  CompanyResponse createEmployeeMappingWithTeamForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestParam("accountId") @NotBlank(message = PARAM_MISSING) String accountId,
      @RequestParam("teamId") @NotBlank(message = PARAM_MISSING) String teamId);

  /**
   * feign服务提供者: 根据账户ID删除账户与公司,团队,项目映射关系,(用于服务间远程调用)
   *
   * @param service 服务消费者描述
   * @param token token令牌
   * @param id 员工id
   * @return CompanyResponse
   */
  @DeleteMapping(path = "/feign/employee/{id}")
  CompanyResponse deleteAccountMappingForFeign(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank(message = PARAM_MISSING) String id);
}
