package com.ben.company.controller;

import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.company.dto.company.NewCompanyDto;
import com.ben.company.service.company.CompanyService;
import com.ben.company.vo.company.CompanyResponse;
import com.ben.company.vo.company.UpdateCompanyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.AuthConstant.*;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;
import static com.ben.common.constant.PermissionConstant.*;

/**
 * @author lomofu
 * @date 2020/1/24 00:13
 */
@Api(tags = "公司接口")
@RestController
@RequestMapping(path = "/api/companies")
@Validated
public class CompanyController {

  @Resource private CompanyService companyService;

  @GetMapping(path = "/companyAndAccount")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据token获取账户信息和公司信息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<CompanyResponse> getAccountAndCompanyByToken(
      @RequestHeader(AUTHORIZATION_HEADER) String token) {
    return new WebAsyncTask<>(() -> companyService.getAccountAndCompanyByToken(token));
  }

  @GetMapping(path = "/company")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据id获取公司信息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<CompanyResponse> getCompanyById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> companyService.getCompanyById(token, id));
  }

  @PutMapping(path = "/company")
  @CheckPerm(value = SYS_COMPANY_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 更新公司信息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "updateCompanyVO",
        value = "updateCompanyVO 视图类",
        dataType = "UpdateCompanyVO",
        required = true)
  })
  public WebAsyncTask<CompanyResponse> updateCompany(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid UpdateCompanyVO updateCompanyVO) {
    return new WebAsyncTask<>(() -> companyService.updateCompany(token, updateCompanyVO));
  }

  @GetMapping(path = "/totalCount")
  @CheckPerm(value = {SYS_EMPLOYEE_VIEW, SYS_TEAM_VIEW, SYS_PROJECT_VIEW})
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 查看统计接口")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "companyId",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<CompanyResponse> getTotalCountByCompanyId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String companyId) {
    return new WebAsyncTask<>(() -> companyService.getTotalCountByCompanyId(token, companyId));
  }

  @DeleteMapping(path = "/feign/employee/{id}")
  @Authorize(value = AUTHORIZATION_SERVICE_ACCOUNT)
  @ApiOperation("feign服务提供者: 根据账户ID删除账户与公司,团队,项目映射关系,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_ACCOUNT,
        required = true),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "员工id",
        dataType = "String",
        required = true)
  })
  public CompanyResponse deleteAccountMappingForFeign(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank(message = PARAM_MISSING) String id) {
    return companyService.deleteAccountMapping(token, id);
  }

  @GetMapping(path = "/feign/company")
  @Authorize(value = AUTHORIZATION_SERVICE_ACCOUNT)
  @ApiOperation("feign服务提供者: 根据账户ID数组查询一组账户信息,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_ACCOUNT,
        required = true),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public CompanyResponse getCompanyByIdForFeign(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return companyService.getCompanyByIdForFeign(token, id);
  }

  @PostMapping(path = "/feign/company")
  @Authorize(value = AUTHORIZATION_SERVICE_ACCOUNT)
  @ApiOperation("feign服务提供者: 创建新公司或者团队,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_ACCOUNT,
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "newCompanyDto",
        value = "newCompanyDto 视图类",
        dataType = "NewCompanyDto",
        required = true)
  })
  public CompanyResponse createNewCompanyForFeign(@RequestBody @Valid NewCompanyDto newCompanyDto) {
    return companyService.createNewCompany(newCompanyDto);
  }

  @GetMapping(path = "/feign/employeeWithCompany")
  @Authorize(value = AUTHORIZATION_SERVICE_ACCOUNT)
  @ApiOperation("feign服务提供者: 创建员工与公司的映射关系,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_ACCOUNT,
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "companyId",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public CompanyResponse createEmployeeMappingWithCompanyForFeign(
      @RequestParam("accountId") @NotBlank(message = PARAM_MISSING) String accountId,
      @RequestParam("companyId") @NotBlank(message = PARAM_MISSING) String companyId) {
    return companyService.createEmployeeMappingWithCompany(accountId, companyId);
  }

  @GetMapping(path = "/feign/employeeWithTeam")
  @Authorize(value = AUTHORIZATION_SERVICE_ACCOUNT)
  @ApiOperation("feign服务提供者: 创建员工与团队的映射关系,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_ACCOUNT,
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "teamId",
        value = "团队id",
        dataType = "String",
        required = true)
  })
  public CompanyResponse createEmployeeMappingWithTeamForFeign(
      @RequestParam("accountId") @NotBlank(message = PARAM_MISSING) String accountId,
      @RequestParam("teamId") @NotBlank(message = PARAM_MISSING) String teamId) {
    return companyService.createEmployeeMappingWithTeam(accountId, teamId);
  }
}
