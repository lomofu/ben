package com.ben.account.controller;

import com.ben.account.dao.AccountDao;
import com.ben.account.dto.AccountDto;
import com.ben.account.dto.PermissionDto;
import com.ben.account.enums.AccountEnum;
import com.ben.account.exception.AccountException;
import com.ben.account.service.AccountService;
import com.ben.account.service.PermissionService;
import com.ben.account.service.RoleService;
import com.ben.account.vo.*;
import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.annotation.PhoneNumber;
import com.ben.common.domain.PageFilter;
import com.ben.common.response.BaseResponse;
import com.ben.common.utils.JwtHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static com.ben.common.constant.AuthConstant.*;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;
import static com.ben.common.constant.PermissionConstant.*;

/**
 * @author lomofu
 * @date 2020-01-18 15:10
 */
@Api(tags = "账户微服务接口")
@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {

  @Resource private AccountDao accountDao;

  @Resource private AccountService accountService;

  @Resource private RoleService roleService;

  @Resource private PermissionService permissionService;

  @GetMapping(path = "/id")
  @CheckPerm(value = SYS_ACCOUNT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据账户ID获取账户信息")
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
        value = "账户id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> accountService.findById(token, id));
  }

  @GetMapping(path = "/phoneNumber")
  @CheckPerm(value = SYS_ACCOUNT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据手机号码获取账户信息")
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
        name = "phoneNumber",
        value = "手机号码",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountByPhoneNumber(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @PhoneNumber @NotBlank(message = PARAM_MISSING) String phoneNumber) {
    return new WebAsyncTask<>(() -> accountService.findByPhoneNumber(token, phoneNumber));
  }

  @GetMapping(path = "/name")
  @CheckPerm(value = SYS_ACCOUNT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据账户名获取账户信息")
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
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_GATEWAY,
        required = true),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "userName",
        value = "账户名",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountByUserName(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String userName) {
    return new WebAsyncTask<>(() -> accountService.findByName(token, userName));
  }

  @GetMapping(path = "/email")
  @CheckPerm(value = SYS_ACCOUNT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据email获取账户信息")
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
        name = "email",
        value = "邮箱",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountByEmail(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @Email @NotBlank(message = PARAM_MISSING) String email) {
    return new WebAsyncTask<>(() -> accountService.findByEmail(token, email));
  }

  @GetMapping(path = "/account")
  @ApiOperation("异步请求: 根据email创建管理员账户")
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
        paramType = "query",
        name = "email",
        value = "邮箱",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> createNewAccount(
      @RequestParam @Email @NotBlank(message = PARAM_MISSING) String email) {
    return new WebAsyncTask<>(() -> accountService.createNewAccount(email));
  }

  @PutMapping(path = "/account")
  @CheckPerm(value = SYS_ACCOUNT_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据updateVO视图类更新账户")
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
        name = "updateVO",
        value = "updateVO视图类",
        dataType = "UpdateVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateAccount(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull UpdateVO updateVO) {
    return new WebAsyncTask<>(() -> accountService.updateAccount(token, updateVO));
  }

  @GetMapping(path = "/tempEmployee")
  @ApiOperation("异步请求: 根据token获取临时未激活员工信息")
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
        paramType = "body",
        name = "token",
        value = "token",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getTempEmployeeAccount(
      @RequestParam @NotBlank String token) {
    return new WebAsyncTask<>(() -> accountService.getTempEmployeeAccount(token));
  }

  @PostMapping(path = "/tempEmployee")
  @CheckPerm(value = SYS_EMPLOYEE_ADD)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据tempEmployee视图类创建临时未激活员工账号")
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
        name = "tempEmployee",
        value = "tempEmployee视图类",
        dataType = "TempEmployee",
        required = true)
  })
  public WebAsyncTask<AccountResponse> createTempEmployeeAccountAndSendEmail(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @NotNull @Valid TempEmployee tempEmployee) {
    return new WebAsyncTask<>(
        () -> accountService.createTempEmployeeAccountAndSendEmail(token, tempEmployee));
  }

  @PostMapping(path = "/employee")
  @ApiOperation("异步请求: 根据employeeVO视图类激活员工账号")
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
        paramType = "body",
        name = "employeeVO",
        value = "employeeVO视图类",
        dataType = "EmployeeVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> createNewEmployee(
      @RequestBody @Valid @NotNull EmployeeVO employeeVO) {
    return new WebAsyncTask<>(() -> accountService.createNewEmployee(employeeVO));
  }

  @PutMapping(path = "/employee")
  @CheckPerm(value = SYS_EMPLOYEE_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据updateVO视图类更新未激活临时员工账户")
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
        paramType = "body",
        name = "updateVO",
        value = "updateVO视图类",
        dataType = "UpdateVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateEmployee(
      @RequestBody @Valid @NotNull UpdateVO updateVO) {
    return new WebAsyncTask<>(() -> accountService.updateEmployee(updateVO));
  }

  @DeleteMapping(path = "/employeeList")
  @CheckPerm(value = SYS_EMPLOYEE_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除正式员工账号")
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
        name = "employeeListId",
        value = "一组员工id列表",
        dataType = "List<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> deleteEmployeeList(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @NotEmpty List<String> employeeListId) {
    return new WebAsyncTask<>(() -> accountService.deleteEmployeeList(token, employeeListId));
  }

  @DeleteMapping(path = "/tempEmployeeList")
  @CheckPerm(value = SYS_EMPLOYEE_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除未激活员工账号")
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
        paramType = "body",
        name = "tempEmployeeListId",
        value = "一组未激活员工id列表",
        dataType = "List<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> deleteTempEmployeeList(
      @RequestBody @NotEmpty List<String> tempEmployeeListId) {
    return new WebAsyncTask<>(() -> accountService.deleteTempEmployeeList(tempEmployeeListId));
  }

  @GetMapping(path = "/isUnique")
  @ApiOperation("异步请求: 查询邮箱 电话 用户名的唯一性")
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
        paramType = "query",
        name = "type",
        value = "类型 'email','phone','name',其他抛出参数绑定错误异常",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "data",
        value = "输入值 对应类型的值",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> isUnique(
      @RequestParam @NotBlank(message = PARAM_MISSING) String type,
      @RequestParam @NotBlank(message = PARAM_MISSING) String data) {
    return new WebAsyncTask<>(() -> accountService.isUnique(type, data));
  }

  @GetMapping(path = "/login")
  @ApiOperation("异步请求: 查询token是否过期合法判断是否为登陆状态")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "query",
        name = "token",
        value = "token",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<BaseResponse> isLogin(@RequestParam String token) {
    return new WebAsyncTask<>(() -> accountService.isLogin(token));
  }

  @PostMapping(path = "/login")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 账户密码登陆")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "body",
        name = "loginVO",
        value = "loginVO",
        dataType = "LoginVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> login(@RequestBody @Valid @NotNull LoginVO loginVO) {
    return new WebAsyncTask<>(() -> accountService.login(loginVO));
  }

  @GetMapping(path = "/code/{phoneNumber}")
  @ApiOperation("异步请求: 获取短信登陆验证码")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "query",
        name = "phoneNumber",
        value = "手机号码",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getLoginCode(@PathVariable @NotBlank String phoneNumber) {
    return new WebAsyncTask<>(() -> accountService.getLoginCode(phoneNumber));
  }

  @PostMapping(path = "/loginWithCode")
  @ApiOperation("异步请求: 短信登陆")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "body",
        name = "loginWithCodeVO",
        value = "loginWithCodeVO视图类",
        dataType = "LoginWithCodeVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> loginWithCode(
      @RequestBody @Valid LoginWithCodeVO loginWithCodeVO) {
    return new WebAsyncTask<>(() -> accountService.loginWithCode(loginWithCodeVO));
  }

  //  @GetMapping(path = "/password")
  //  public WebAsyncTask<AccountResponse> resetPassword(
  //      @RequestParam("token") @NotBlank String token,
  //      @RequestParam("password") @NotBlank String password) {
  //    return new WebAsyncTask<>(() -> accountService.resetPassword(token, password));
  //  }

  @PostMapping(path = "/avatar")
  @CheckPerm(value = SYS_ACCOUNT_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 更新账户头像")
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
        name = "multipartFile",
        value = "multipartFile base64编码图片文件",
        dataType = "MultipartFile",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateAvatar(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam("file") MultipartFile multipartFile) {
    return new WebAsyncTask<>(
        () -> {
          String accountId = JwtHelper.verifyToken(token).get(CLAIM_ID).asString();
          UpdateVO updateVO =
              Optional.ofNullable(accountDao.findByIdAndActiveTrue(accountId))
                  .map(
                      e ->
                          UpdateVO.builder()
                              .email(e.getEmail())
                              .name(e.getName())
                              .phoneNumber(e.getPhoneNumber())
                              .id(e.getId()))
                  .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_IS_EMPTY))
                  .build();
          return accountService.updateAvatar(token, updateVO, multipartFile);
        });
  }

  @PutMapping(path = "/accountRole")
  @CheckPerm(value = SYS_EMPLOYEE_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 更新非管理员角色")
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
        name = "updateAccountRoleVO",
        value = "updateAccountRoleVO视图类",
        dataType = "UpdateAccountRoleVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateAccountRole(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid UpdateAccountRoleVO updateAccountRoleVO) {
    return new WebAsyncTask<>(() -> accountService.updateAccountRole(token, updateAccountRoleVO));
  }

  @GetMapping(path = "/accountListByCompanyId")
  @CheckPerm(value = SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据公司id查看公司员工列表")
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
        name = "active",
        value = "是正式员工列表还是未激活员工列表",
        dataType = "boolean",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountListByCompanyId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      PageFilter<String> pageFilter,
      @RequestParam(name = "active") boolean active) {
    return new WebAsyncTask<>(
        () -> accountService.findAccountListByCompanyId(token, pageFilter, active));
  }

  @GetMapping(path = "/accountListByTeamId")
  @CheckPerm(value = SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据团队id查看公司员工列表")
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
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "active",
        value = "是正式员工列表还是未激活员工列表",
        dataType = "boolean",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountListByTeamId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      PageFilter<String> pageFilter,
      @RequestParam(name = "active") boolean active) {
    return new WebAsyncTask<>(
        () -> accountService.findAccountListByTeamId(token, pageFilter, active));
  }

  @GetMapping(path = "/accountListByProjectId")
  @CheckPerm(value = SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据项目id查看公司员工列表")
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
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getAccountListByProjectId(
      @RequestHeader(AUTHORIZATION_HEADER) String token, PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> accountService.findAccountListByProjectId(token, pageFilter));
  }

  @GetMapping(path = "/simpleAccountListByProjectId")
  @CheckPerm(value = SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据项目id查看简易公司员工列表(用于渲染排班表的左侧成员)")
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
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getSimpleAccountListByProjectId(
      @RequestHeader(AUTHORIZATION_HEADER) String token, PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(
        () -> accountService.findSimpleAccountListByProjectId(token, pageFilter));
  }

  @GetMapping(path = "/roleList")
  @CheckPerm(value = SYS_PERMISSION_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据项目公司id查看公司所含角色列表")
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
        paramType = "query",
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getRoleListByCompanyId(PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> roleService.findRoleListByCompanyId(pageFilter));
  }

  @GetMapping(path = "/simpleRoleList/{companyId}")
  @CheckPerm(value = SYS_PERMISSION_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 根据项目公司id查看公司所含简单角色列表(用于角色下拉选择列表)")
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
        paramType = "query",
        name = "companyId",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getSimpleRoleListByCompanyId(
      @PathVariable @NotBlank(message = PARAM_MISSING) String companyId) {
    return new WebAsyncTask<>(() -> roleService.findSimpleRoleListByCompanyId(companyId));
  }

  @PostMapping(path = "/role")
  @CheckPerm(value = SYS_ROLE_ADD)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 创建新的角色")
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
        paramType = "body",
        name = "newRoleVO",
        value = "newRoleVO视图累",
        dataType = "NewRoleVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> createNewRole(
      @RequestBody @Valid @NotNull NewRoleVO newRoleVO) {
    return new WebAsyncTask<>(() -> roleService.createNewRole(newRoleVO));
  }

  @PutMapping(path = "/role")
  @CheckPerm(value = SYS_ROLE_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 修改角色")
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
        paramType = "body",
        name = "updateRoleVO",
        value = "updateRoleVO视图累",
        dataType = "UpdateRoleVO",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateRole(
      @RequestBody @Valid @NotNull UpdateRoleVO updateRoleVO) {
    return new WebAsyncTask<>(() -> roleService.updateRole(updateRoleVO));
  }

  @DeleteMapping(path = "/role/{id}")
  @CheckPerm(value = SYS_ROLE_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 删除角色")
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
        paramType = "path",
        name = "id",
        value = "角色id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> deleteRole(
      @PathVariable @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> roleService.deleteRole(id));
  }

  @PutMapping(path = "/rolePerm/{roleId}")
  @CheckPerm(value = SYS_ROLE_PERMISSION_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 修改角色权限")
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
        paramType = "path",
        name = "roleId",
        value = "角色id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "permIdList",
        value = "权限id列表",
        dataType = "List",
        required = true)
  })
  public WebAsyncTask<AccountResponse> updateRolePerms(
      @PathVariable @NotBlank(message = PARAM_MISSING) String roleId,
      @RequestBody List<String> permIdList) {
    return new WebAsyncTask<>(() -> roleService.updateRolePerms(roleId, permIdList));
  }

  @DeleteMapping(path = "/roleList")
  @CheckPerm(value = SYS_ROLE_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除角色")
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
        paramType = "body",
        name = "roleListId",
        value = "角色id列表",
        dataType = "List",
        required = true)
  })
  public WebAsyncTask<AccountResponse> deleteRolesByRoleList(
      @RequestBody @NotEmpty List<String> roleListId) {
    return new WebAsyncTask<>(() -> roleService.deleteRolesByRoleList(roleListId));
  }

  @GetMapping(path = "/permListByRoleId/{id}")
  @CheckPerm(value = SYS_PERMISSION_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取角色的权限集")
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
        paramType = "path",
        name = "id",
        value = "角色id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getPermListByRoleId(
      @PathVariable @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> permissionService.getPermListByRoleId(id));
  }

  @GetMapping(path = "/permList")
  @CheckPerm(value = SYS_PERMISSION_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取系统所有权限集")
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
        dataType = "String")
  })
  public WebAsyncTask<AccountResponse> getAllPermList() {
    return new WebAsyncTask<>(() -> permissionService.getAllPermList());
  }

  @GetMapping(path = "/setting/{accountId}")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取账户的设置菜单")
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
        paramType = "path",
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<AccountResponse> getSettingMenuByAccountId(
      @PathVariable @NotBlank(message = PARAM_MISSING) String accountId) {
    return new WebAsyncTask<>(() -> permissionService.getSettingMenu(accountId));
  }

  @GetMapping(path = "/feign/id")
  @Authorize(value = AUTHORIZATION_SERVICE_COMPANY)
  @ApiOperation("feign服务提供者: 根据账户ID获取账户信息(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_COMPANY,
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
        value = "账户id",
        dataType = "String",
        required = true)
  })
  public AccountResponse getAccountByIdForFeign(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return accountService.findById(token, id);
  }

  @GetMapping(path = "/feign/{id}")
  @ApiOperation("feign服务提供者: 根据账户ID获取账户信息(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "path",
        name = "id",
        value = "账户id",
        dataType = "String",
        required = true)
  })
  public AccountDto getAccountByIdForFeign(@PathVariable @NotBlank String id) {
    return accountService.findJustAccountById(id);
  }

  @GetMapping(path = "/feign/list")
  @Authorize(value = AUTHORIZATION_SERVICE_COMPANY)
  @ApiOperation("feign服务提供者: 根据账户ID数组查询一组账户信息,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_COMPANY,
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "list",
        value = "一组账户id数组",
        dataType = "String[]",
        required = true)
  })
  public List<AccountDto> getAccountByListIdForFeign(@RequestParam @NotEmpty String[] list) {
    return accountService.findByListId(list);
  }

  @GetMapping(path = "/feign/perm")
  @Authorize(value = AUTHORIZATION_SERVICE_COMPANY)
  @ApiOperation("feign服务提供者: 根据账户ID数组查询该账户的权限集,(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_COMPANY,
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "账户id",
        dataType = "String",
        required = true)
  })
  public List<PermissionDto> getPermissionByAccountIdForFeign(@RequestParam String id) {
    return accountService.getPermissionByAccountId(id);
  }

  @DeleteMapping(path = "/feign/teamMapping")
  @Authorize(value = AUTHORIZATION_SERVICE_COMPANY)
  @ApiOperation("feign服务提供者: 批量删除团队中未激活员工账号(用于服务间远程调用)")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务消费者描述",
        dataType = "String",
        defaultValue = AUTHORIZATION_SERVICE_COMPANY,
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "tempEmployeeListId",
        value = "一组未激活员工id列表",
        dataType = "List<String>",
        required = true)
  })
  public AccountResponse deleteTempEmployeeMappingAccount(
      @RequestBody @NotEmpty List<String> list) {
    return accountService.deleteTempEmployeeMappingAccount(list);
  }
}
