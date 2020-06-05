package com.ben.account.service.impl;

import com.ben.account.config.AppConfiguration;
import com.ben.account.config.OssConfiguration;
import com.ben.account.dao.*;
import com.ben.account.domain.*;
import com.ben.account.dto.*;
import com.ben.account.enums.AccountEnum;
import com.ben.account.exception.AccountException;
import com.ben.account.service.AccountService;
import com.ben.account.service.PermissionService;
import com.ben.account.service.RoleService;
import com.ben.account.utils.PageFilterForJpaHelper;
import com.ben.account.vo.*;
import com.ben.bot.client.BotMailClient;
import com.ben.bot.client.BotSmsClient;
import com.ben.bot.dto.mail.CreateNewEmployeeDto;
import com.ben.bot.dto.mail.CreateNewEmployeeSuccessDto;
import com.ben.bot.dto.mail.LoginToMuchDto;
import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.bot.vo.BotResponse;
import com.ben.common.annotation.CacheExpire;
import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.exception.CustomException;
import com.ben.common.response.BaseResponse;
import com.ben.common.utils.*;
import com.ben.company.client.CompanyClient;
import com.ben.company.dto.company.CompanyFeignDto;
import com.ben.company.dto.company.NewCompanyDto;
import com.ben.company.vo.company.CompanyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ben.account.enums.AccountEnum.*;
import static com.ben.account.validator.ValidatorEnum.*;
import static com.ben.common.constant.AuthConstant.*;
import static com.ben.common.constant.CommonConstant.*;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.common.enums.ResultCode.*;
import static org.apache.http.HttpStatus.SC_MOVED_PERMANENTLY;

/**
 * @author lomofu
 * @date 2020-01-18 15:10
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "account-svc")
public class AccountServiceImpl implements AccountService {
  public static final int MAX_FAIL_LOGIN = 9;

  public static final int MAX_LOGIN_LIMIT = 8;

  public static final long LOGIN_CODE_EXPIRE_TIME = 300L;

  public static final String LOGIN_CODE_PREFIX = "login::code:";

  public static final String LOCK_UPDATE_ACCOUNT_ROLE = "LockUpdateAccountRole";

  public static final String DELETE_TEMP_EMPLOYEE = "deleteTempEmployee";

  public static final String DELETE_EMPLOYEE = "deleteEmployee";

  public static final String CREATE_NEW_ACCOUNT = "createNewAccount";

  public static final String UPDATE_ACCOUNT = "updateAccount";

  public static final String CREATE_NEW_EMPLOYEE = "createNewEmployee";

  public static final String UPDATE_AVATAR = "updateAvatar";

  public static final String ROLE_NAME = "雇员";

  private static Pattern patternEmail = Pattern.compile(EMAIL_REGX);

  private static Pattern patternPhone = Pattern.compile(PHONE_REGX);

  @Resource private AccountDao accountDao;

  @Resource private AccountRoleMappingDao accountRoleMappingDao;

  @Resource private AccountMongodbDao accountMongodbDao;

  @Resource private MailMongodbDao mailMongodbDao;

  @Resource private PasswordMongodbDao passwordMongodbDao;

  @Resource private EmployeeMongodbDao employeeMongodbDao;

  @Resource private CompanyClient companyClient;

  @Resource private BotMailClient botMailClient;

  @Resource private BotSmsClient botSmsClient;

  @Resource private RoleService roleService;

  @Resource private PermissionService permissionService;

  @Resource private RedissonDistributedLocker locker;

  @Resource private OssHelper ossHelper;

  @Resource private RedisHelper redisHelper;

  @Resource private ObjectMapper objectMapper;

  @Resource private AppConfiguration appConfiguration;

  @Resource private OssConfiguration ossConfiguration;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTempEmployee(String id) {
    String key = DELETE_TEMP_EMPLOYEE.concat(id);
    try {
      locker.lock(key, 10L);
      employeeMongodbDao.deleteById(id);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new AccountException(TEMP_ACCOUNT_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @GlobalTransactional(name = "delete-employee", rollbackFor = Exception.class)
  public void deleteEmployee(String token, String id) {
    String key = DELETE_EMPLOYEE.concat(id);
    try {
      locker.lock(key, 10L);
      int account = accountDao.deleteEmployeeAccount(id);
      CompanyResponse response =
          companyClient.deleteAccountMappingForFeign(AUTHORIZATION_SERVICE_ACCOUNT, token, id);
      if (account > 0 && SUCCESS.getCode() == response.getCode()) {
        return;
      }
      throw new AccountException(ACCOUNT_DELETE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new AccountException(ACCOUNT_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Cacheable(
      key = "#id.concat(':').concat(#root.methodName)",
      condition = " #id!=null && #id.length()>0",
      unless = "#result == null")
  @CacheExpire(value = 300L)
  public AccountDto findJustAccountById(String id) {
    return accountDao
        .findById(id)
        .map(
            e ->
                AccountDto.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .email(e.getEmail())
                    .phoneNumber(e.getPhoneNumber())
                    .isAdmin(e.isAdmin())
                    .isActive(e.isActive())
                    .avatarUrl(e.getAvatarUrl())
                    .gender(e.getGender())
                    .createTime(e.getCreateTime())
                    .build())
        .orElse(null);
  }

  @Override
  @Cacheable(
      key = "#id.concat(':').concat(#root.methodName)",
      condition = "#token!=null && #token.length()>0 && #id!=null && #id.length()>0",
      unless = "#result == null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public AccountResponse findById(String token, String id) {
    CompletableFuture<List<String>> permissionListFuture;
    String isAdmin = JwtHelper.verifyToken(token).get(CLAIM_ROLE).asString();
    CompletableFuture<List<String>> roleListFuture = this.getRoleListCompletableFuture(id);
    if (ROLE_ADMIN.equalsIgnoreCase(isAdmin)) {
      permissionListFuture =
          CompletableFuture.supplyAsync(
                  () -> permissionService.findAll(), appConfiguration.getAsyncExecutor())
              .thenApply(e -> e.stream().map(Permission::getPerm).collect(Collectors.toList()));
    } else {
      permissionListFuture = this.getPermissionListCompletableFuture(id);
    }
    return Optional.ofNullable(accountDao.findByIdAndActiveTrue(id))
        .map(e -> this.getAccountDto(roleListFuture, permissionListFuture, e))
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  public List<AccountDto> findByListId(String[] list) {
    if (Objects.isNull(list)) {
      throw new CustomException(PARAM_MISS);
    }
    return Arrays.stream(list)
        .map(e -> accountDao.findByIdAndActiveTrue(e))
        .map(
            v ->
                AccountDto.builder()
                    .id(v.getId())
                    .name(v.getName())
                    .avatarUrl(v.getAvatarUrl())
                    .createTime(v.getCreateTime())
                    .gender(v.getGender())
                    .email(v.getEmail())
                    .phoneNumber(v.getPhoneNumber())
                    .isActive(v.isActive())
                    .isAdmin(v.isAdmin())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<PermissionDto> getPermissionByAccountId(String id) {
    return permissionService.findPermissionListByAccountId(id).stream()
        .map(e -> PermissionDto.builder().id(e.getId()).perm(e.getPerm()).build())
        .collect(Collectors.toList());
  }

  @Override
  public AccountResponse findByEmail(String token, String email) {
    String id = this.getAccountIdByToken(token);

    CompletableFuture<List<String>> roleListCompletableFuture =
        this.getRoleListCompletableFuture(id);

    CompletableFuture<List<String>> permissionListCompletableFuture =
        this.getPermissionListCompletableFuture(id);

    return Optional.ofNullable(accountDao.findByEmailAndActiveTrue(email))
        .map(e -> this.getAccountDto(roleListCompletableFuture, permissionListCompletableFuture, e))
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  public AccountResponse findByPhoneNumber(String token, String phoneNumber) {
    String id = this.getAccountIdByToken(token);

    CompletableFuture<List<String>> roleListCompletableFuture =
        this.getRoleListCompletableFuture(id);

    CompletableFuture<List<String>> permissionListCompletableFuture =
        this.getPermissionListCompletableFuture(id);

    return Optional.ofNullable(accountDao.findByPhoneNumberAndActiveTrue(phoneNumber))
        .map(e -> this.getAccountDto(roleListCompletableFuture, permissionListCompletableFuture, e))
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  public AccountResponse findByName(String token, String name) {
    String id = this.getAccountIdByToken(token);

    CompletableFuture<List<String>> roleListCompletableFuture =
        this.getRoleListCompletableFuture(id);

    CompletableFuture<List<String>> permissionListCompletableFuture =
        this.getPermissionListCompletableFuture(id);
    return Optional.ofNullable(accountDao.findByNameAndActiveTrue(name))
        .map(e -> this.getAccountDto(roleListCompletableFuture, permissionListCompletableFuture, e))
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  public AccountResponse findAccountListByCompanyId(
      String token, PageFilter<String> pageFilter, boolean active) {
    if (active) {
      return Optional.ofNullable(
              accountDao.findAccountListByCompanyId(
                  pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
          .map(
              e ->
                  PageVO.<EmployeeDto>builder()
                      .pages(e.getTotalPages())
                      .total(e.getTotalElements())
                      .list(this.getEmployeeDtoList(e.get()))
                      .build())
          .map(AccountResponse::new)
          .orElseThrow(() -> new AccountException(WITHOUT_RES));
    } else {
      return Optional.ofNullable(
              employeeMongodbDao.findByCompanyId(
                  pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
          .map(
              e ->
                  PageVO.<EmployeeDto>builder()
                      .pages(e.getTotalPages())
                      .total(e.getTotalElements())
                      .list(this.getEmployeeDtoListWithInactive(e.get()))
                      .build())
          .map(AccountResponse::new)
          .orElseThrow(() -> new AccountException(WITHOUT_RES));
    }
  }

  @Override
  public AccountResponse findAccountListByTeamId(
      String token, PageFilter<String> pageFilter, boolean active) {
    if (active) {
      return Optional.ofNullable(
              accountDao.findAccountListByTeamId(
                  pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
          .map(
              e ->
                  PageVO.<EmployeeDto>builder()
                      .pages(e.getTotalPages())
                      .total(e.getTotalElements())
                      .list(this.getEmployeeDtoList(e.get()))
                      .build())
          .map(AccountResponse::new)
          .orElseThrow(() -> new AccountException(WITHOUT_RES));
    } else {
      return Optional.ofNullable(
              employeeMongodbDao.findByTeamId(
                  pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
          .map(
              e ->
                  PageVO.<EmployeeDto>builder()
                      .pages(e.getTotalPages())
                      .total(e.getTotalElements())
                      .list(this.getEmployeeDtoListWithInactive(e.get()))
                      .build())
          .map(AccountResponse::new)
          .orElseThrow(() -> new AccountException(WITHOUT_RES));
    }
  }

  @Override
  public AccountResponse findAccountListByProjectId(String token, PageFilter<String> pageFilter) {
    return Optional.ofNullable(
            accountDao.findAccountListByProjectId(
                pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
        .map(
            e ->
                PageVO.<EmployeeDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.getEmployeeDtoList(e.get()))
                    .build())
        .map(AccountResponse::new)
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  @Cacheable(
      key =
          "#token.concat(':page:').concat(#root.methodName).concat(':').concat(#pageFilter.getData()).concat(':')"
              + ".concat(#pageFilter.getSortBy()).concat(#pageFilter.isSortDesc()).concat(#pageFilter.getPageSize())",
      condition =
          "#token!=null && #token.length()>0  && #pageFilter!=null "
              + "&& (#pageFilter.getPageNumber() == 1 || #pageFilter.getPageNumber()==2) "
              + "&& (#pageFilter.getPageSize()==5 || #pageFilter.getPageSize()==10 || #pageFilter.getPageSize()==50 )",
      unless = "#result == null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public AccountResponse findSimpleAccountListByProjectId(
      String token, PageFilter<String> pageFilter) {
    return Optional.ofNullable(
            accountDao.findAccountListByProjectId(
                pageFilter.getData(), PageFilterForJpaHelper.getJpaPageRequest(pageFilter)))
        .map(
            e ->
                PageVO.<SimpleAccountDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.getSimpleAccountDtoList(e.get()))
                    .hasNextPage(e.hasNext())
                    .build())
        .map(AccountResponse::new)
        .orElseThrow(() -> new AccountException(WITHOUT_RES));
  }

  @Override
  @GlobalTransactional(name = "create-new-account", rollbackFor = Exception.class)
  public AccountResponse createNewAccount(String email) {
    String key = CREATE_NEW_ACCOUNT.concat(email);
    // 从mongodb中获取注册表单的暂存消息
    MongoAccount data =
        Optional.ofNullable(accountMongodbDao.findByEmail(email))
            .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));

    if (this.emailValidator(data.getEmail())
        || this.nameValidator(data.getName())
        || this.phoneNumberValidator(data.getPhoneNumber())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    Account account =
        Account.builder()
            .name(data.getName())
            .gender(data.getGender())
            .password(MD5Helper.passwordMD5(data.getPassword()))
            .email(email)
            .phoneNumber(data.getPhoneNumber())
            .avatarUrl(AvatarHelper.generaDefaultAvatar(email))
            .createTime(new Date())
            .admin(true)
            .active(true)
            .build();
    try {
      locker.lock(key, 30L);
      Account save = accountDao.save(account);
      roleService.addRoleForAccountByRoleName(ROLE_ADMIN, save.getId());
      // 创建新公司
      CompanyResponse response =
          companyClient.createNewCompanyForFeign(
              AUTHORIZATION_SERVICE_ACCOUNT,
              NewCompanyDto.builder()
                  .accountId(save.getId())
                  .type(data.getType())
                  .companyName(data.getCompanyName())
                  .companyAddress(data.getCompanyAddress())
                  .build());

      if (SUCCESS.getCode() != response.getCode()) {
        throw new AccountException(ACCOUNT_CREATE_FAIL);
      } else {
        // 异步删除mdb中数据,发送注册成功邮件
        CompletableFuture.runAsync(
                () -> {
                  botMailClient.sendEmailWithCreateNewAdminSuccess(
                      AUTHORIZATION_SERVICE_ACCOUNT,
                      CreateNewEmployeeSuccessDto.builder()
                          .email(data.getEmail())
                          .name(data.getName())
                          .build());
                  accountMongodbDao.deleteById(data.getId());
                  mailMongodbDao.delete(MongoEmail.builder().email(data.getEmail()).build());
                },
                appConfiguration.getAsyncExecutor())
            .exceptionally(
                e -> {
                  log.error(
                      METHOD_ERROR_TEMPLATE,
                      Thread.currentThread().getName(),
                      this.getClass().getName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      Thread.currentThread().getStackTrace()[1].getLineNumber(),
                      e.getMessage());
                  return null;
                });
        return new AccountResponse(ACCOUNT_CREATE_SUCCESS);
      }
    } catch (Exception e) {
      throw new AccountException(ACCOUNT_CREATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#updateVO.getId().concat(':findById:')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true)
      })
  public AccountResponse updateAccount(String token, UpdateVO updateVO) {
    Account account =
        Optional.ofNullable(updateVO)
            .flatMap(e -> accountDao.findById(e.getId()))
            .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));

    if (!account.getEmail().equals(updateVO.getEmail())
        && this.emailValidator(updateVO.getEmail())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    if (!account.getPhoneNumber().equals(updateVO.getPhoneNumber())
        && this.phoneNumberValidator(updateVO.getPhoneNumber())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    if (!account.getName().equals(updateVO.getName()) && this.nameValidator(updateVO.getName())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    String key = UPDATE_ACCOUNT.concat(account.getId());
    try {
      locker.lock(key);
      int update =
          accountDao.update(
              updateVO.getName(),
              updateVO.getGender(),
              updateVO.getEmail(),
              updateVO.getPhoneNumber(),
              updateVO.getId());
      if (update > 0) {
        return new AccountResponse(ACCOUNT_UPDATE_SUCCESS, updateVO.getId());
      } else {
        throw new AccountException(ACCOUNT_UPDATE_FAIL);
      }
    } catch (Exception e) {
      throw new AccountException(ACCOUNT_UPDATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public AccountResponse getTempEmployeeAccount(String token) {
    return Optional.ofNullable(employeeMongodbDao.findByToken(token))
        .map(AccountResponse::new)
        .orElseThrow(() -> new AccountException(ACCOUNT_TIME_EXPIRE));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse createTempEmployeeAccountAndSendEmail(
      String token, TempEmployee tempEmployee) {

    // 查询是否有存在的账户
    if (this.emailValidator(tempEmployee.getEmail())
        || this.nameValidator(tempEmployee.getName())
        || this.phoneNumberValidator(tempEmployee.getPhoneNumber())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    // 构建mongo bean
    MongoEmployee mongoEmployee =
        Optional.of(tempEmployee)
            .map(
                e ->
                    MongoEmployee.builder()
                        .name(e.getName())
                        .email(e.getEmail())
                        .phoneNumber(e.getPhoneNumber())
                        .companyId(e.getCompanyId())
                        .avatarUrl(AvatarHelper.generaDefaultAvatar(e.getEmail()))
                        .gender(e.getGender())
                        .active(false)
                        .admin(false)
                        .teamId(e.getTeamId())
                        .companyId(e.getCompanyId())
                        .createTime(new Date())
                        .token(
                            JwtHelper.createTempToken(
                                e.getEmail(), TimeHelper.addMinutes(new Date(), 30L)))
                        .expireAt(TimeHelper.addMinutes(new Date(), 30L))
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));

    // 异步查询管理员名称
    CompletableFuture<String> from =
        CompletableFuture.supplyAsync(
            () ->
                accountDao
                    .findById(tempEmployee.getAdminId())
                    .map(Account::getName)
                    .orElseThrow(() -> new CustomException(PARAM_MISS)),
            appConfiguration.getAsyncExecutor());

    // 异步查询公司名称
    CompletableFuture<String> companyName =
        CompletableFuture.supplyAsync(
            () ->
                Optional.ofNullable(
                        companyClient.getCompanyByIdForFeign(
                            AUTHORIZATION_SERVICE_ACCOUNT, token, mongoEmployee.getCompanyId()))
                    .map(e -> objectMapper.convertValue(e.getData(), CompanyFeignDto.class))
                    .map(CompanyFeignDto::getName)
                    .orElseThrow(() -> new CustomException(PARAM_MISS)),
            appConfiguration.getAsyncExecutor());

    String key = "createTempEmployeeAccountAndSendEmail".concat(mongoEmployee.getEmail());
    try {
      locker.lock(key, 10L);
      // 临时放入mongodb中
      employeeMongodbDao.save(mongoEmployee);
      // 异步发送邮件
      CompletableFuture.runAsync(
          () ->
              botMailClient.sendEmailWithCreateNewEmployee(
                  AUTHORIZATION_SERVICE_ACCOUNT,
                  CreateNewEmployeeDto.builder()
                      .from(from.join())
                      .to(mongoEmployee.getEmail())
                      .companyName(companyName.join())
                      .token(mongoEmployee.getToken())
                      .build()),
          appConfiguration.getAsyncExecutor());
      return new AccountResponse(CREATE_EMPLOYEE_SUCCESS);
    } catch (Exception e) {
      employeeMongodbDao.delete(mongoEmployee);
      throw new AccountException(CREATE_EMPLOYEE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @GlobalTransactional(name = "create-new-employee", rollbackFor = Exception.class)
  public AccountResponse createNewEmployee(EmployeeVO employeeVO) {
    Account account =
        Optional.ofNullable(employeeVO)
            .map(
                e ->
                    Account.builder()
                        .name(e.getName())
                        .gender(e.getGender())
                        .email(e.getEmail())
                        .phoneNumber(e.getPhoneNumber())
                        .avatarUrl(e.getAvatarUrl())
                        .password(MD5Helper.passwordMD5(e.getPassword()))
                        .active(true)
                        .admin(e.isAdmin())
                        .createTime(new Date())
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));
    if (this.emailValidator(account.getEmail())
        || this.nameValidator(account.getName())
        || this.phoneNumberValidator(account.getPhoneNumber())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    String key = CREATE_NEW_EMPLOYEE.concat(employeeVO.getEmail());
    try {
      locker.lock(key, 30L);
      Account save = accountDao.save(account);
      roleService.addRoleForAccountByRoleName(ROLE_NAME, save.getId());
      CompanyResponse response =
          Optional.ofNullable(save.getId())
              .map(
                  e ->
                      companyClient.createEmployeeMappingWithCompanyForFeign(
                          AUTHORIZATION_SERVICE_ACCOUNT, save.getId(), employeeVO.getCompanyId()))
              .orElseThrow(() -> new AccountException(CREATE_EMPLOYEE_FAIL));
      if (SUCCESS.getCode() != response.getCode()) {
        throw new AccountException(CREATE_EMPLOYEE_FAIL);
      }
      if (StringUtils.hasText(employeeVO.getTeamId())) {
        CompanyResponse response1 =
            companyClient.createEmployeeMappingWithTeamForFeign(
                AUTHORIZATION_SERVICE_ACCOUNT, save.getId(), employeeVO.getTeamId());
        if (SUCCESS.getCode() != response1.getCode()) {
          throw new AccountException(CREATE_EMPLOYEE_FAIL);
        }
      }
      return new AccountResponse(CREATE_EMPLOYEE_SUCCESS);
    } catch (Exception e) {
      throw new AccountException(CREATE_EMPLOYEE_FAIL);
    } finally {
      employeeMongodbDao.deleteMongoEmployeeByEmailAndNameAndPhoneNumber(
          employeeVO.getEmail(), employeeVO.getName(), employeeVO.getPhoneNumber());
      locker.unlock(key);
    }
  }

  @Override
  public AccountResponse updateEmployee(UpdateVO updateVO) {
    MongoEmployee mongoEmployee =
        Optional.ofNullable(updateVO)
            .flatMap(e -> employeeMongodbDao.findById(updateVO.getId()))
            .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));

    if (!mongoEmployee.getEmail().equals(updateVO.getEmail())
        && this.emailValidator(updateVO.getEmail())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    if (!mongoEmployee.getName().equals(updateVO.getEmail())
        && this.nameValidator(updateVO.getName())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    if (!mongoEmployee.getPhoneNumber().equals(updateVO.getEmail())
        && this.phoneNumberValidator(updateVO.getPhoneNumber())) {
      throw new AccountException(ACCOUNT_EXIST);
    }

    String key = "updateEmployee".concat(updateVO.getId());
    try {
      locker.lock(key, 10L);
      MongoEmployee employee =
          employeeMongodbDao
              .findById(updateVO.getId())
              .map(
                  e ->
                      e.toBuilder()
                          .name(updateVO.getName())
                          .email(e.getEmail())
                          .gender(updateVO.getGender())
                          .avatarUrl(AvatarHelper.generaDefaultAvatar(updateVO.getEmail()))
                          .phoneNumber(updateVO.getPhoneNumber())
                          .build())
              .map(e -> employeeMongodbDao.save(e))
              .orElseThrow(() -> new AccountException(ACCOUNT_IS_EMPTY));
      return new AccountResponse(ACCOUNT_UPDATE_SUCCESS, employee);
    } catch (Exception e) {
      employeeMongodbDao.save(mongoEmployee);
      throw new AccountException(ACCOUNT_UPDATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @CacheEvict(key = "#token.concat(':page')", allEntries = true)
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse deleteEmployeeList(String token, List<String> employeeListId) {
    try {
      employeeListId.forEach(e -> this.deleteEmployee(token, e));
      if (employeeListId.size() > 1) {
        return new AccountResponse(ACCOUNT_LIST_DELETE_SUCCESS);
      }
      return new AccountResponse(ACCOUNT_DELETE_SUCCESS);
    } catch (Exception e) {
      if (employeeListId.size() > 1) {
        throw new AccountException(ACCOUNT_LIST_DELETE_FAIL);
      }
      throw new AccountException(ACCOUNT_DELETE_FAIL);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse deleteTempEmployeeList(List<String> tempEmployeeListId) {
    try {
      tempEmployeeListId.forEach(this::deleteTempEmployee);
      if (tempEmployeeListId.size() > 1) {
        return new AccountResponse(TEMP_ACCOUNT_LIST_DELETE_SUCCESS);
      }
      return new AccountResponse(TEMP_ACCOUNT_DELETE_SUCCESS);
    } catch (Exception e) {
      if (tempEmployeeListId.size() > 1) {
        throw new AccountException(TEMP_ACCOUNT_LIST_DELETE_FAIL);
      }
      throw new AccountException(TEMP_ACCOUNT_DELETE_FAIL);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse deleteTempEmployeeMappingAccount(List<String> list) {
    try {
      list.forEach(
          e ->
              employeeMongodbDao
                  .findById(e)
                  .map(i -> i.toBuilder().teamId(null).build())
                  .map(v -> employeeMongodbDao.save(v)));
      return new AccountResponse(TEMP_ACCOUNT_REMOVE_TEAM_SUCCESS);
    } catch (Exception e) {
      throw new AccountException(TEAM_ACCOUNT_REMOVE_TEAM_FAIL);
    }
  }

  @Override
  public AccountResponse login(LoginVO loginVO) {
    String key = "login::".concat(loginVO.getAccount());
    int count =
        Optional.ofNullable(objectMapper.convertValue(redisHelper.get(key), Integer.class))
            .orElse(0);
    if (count > MAX_LOGIN_LIMIT) {
      if (patternEmail.matcher(loginVO.getAccount()).find() && count == MAX_FAIL_LOGIN) {
        CompletableFuture.runAsync(
            () ->
                botMailClient.sendEmailWithLoginToMuch(
                    AUTHORIZATION_SERVICE_ACCOUNT,
                    LoginToMuchDto.builder()
                        .email(loginVO.getAccount())
                        .account(loginVO.getAccount())
                        .build()),
            appConfiguration.getAsyncExecutor());
        redisHelper.set(key, count + 1, DEFAULT_CACHE_TIME);
      }

      if (patternPhone.matcher(loginVO.getAccount()).find() && count == MAX_FAIL_LOGIN) {
        CompletableFuture.runAsync(
            () ->
                botSmsClient.sendSMSWithLoginToMuch(
                    AUTHORIZATION_SERVICE_ACCOUNT, loginVO.getAccount()),
            appConfiguration.getAsyncExecutor());
        redisHelper.set(key, count + 1, DEFAULT_CACHE_TIME);
      }
      throw new AccountException(AccountEnum.TOO_MUCH_TIME_LOGIN);
    }
    return Optional.ofNullable(LoginContext.login(accountDao, loginVO))
        .orElseGet(
            () -> {
              redisHelper.set(key, count + 1, DEFAULT_CACHE_TIME);
              throw new AccountException(ERROR_ACCOUNT_OR_PASSWORD);
            });
  }

  @Override
  public AccountResponse loginWithCode(LoginWithCodeVO loginWithCodeVO) {
    String key = LOGIN_CODE_PREFIX.concat(loginWithCodeVO.getPhoneNumber());
    String result = (String) redisHelper.get(key);
    if (Objects.nonNull(result) && result.equals(loginWithCodeVO.getCode())) {
      redisHelper.del(key);
      return LoginContext.loginWithCode(
          accountDao, loginWithCodeVO.getPhoneNumber(), loginWithCodeVO.isRememberMe());
    } else {
      throw new AccountException(SMS_CODE_LOGIN_FAIL);
    }
  }

  @Override
  public AccountResponse getLoginCode(String phoneNumber) {
    String key = LOGIN_CODE_PREFIX.concat(phoneNumber);
    if (redisHelper.hasKey(key)) {
      throw new AccountException(SMS_HAS_EXIST);
    }
    Account account = accountDao.findByPhoneNumberAndActiveTrue(phoneNumber);
    if (Objects.nonNull(account)) {
      String code = CodeHelper.generateRandomNumber(6);

      redisHelper.set(key, code, LOGIN_CODE_EXPIRE_TIME);
      BotResponse response =
          botSmsClient.sendSMSWithLoginWithPhoneCode(
              AUTHORIZATION_SERVICE_ACCOUNT,
              LoginWithPhoneCodeDto.builder().phoneNumber(phoneNumber).code(code).build());
      if (SUCCESS.getCode() == response.getCode()) {
        return new AccountResponse(SMS_SEND_SUCCESS);
      }
      redisHelper.del(key);
      throw new AccountException(SMS_SEND_FAIL);
    } else {
      throw new AccountException(ACCOUNT_IS_EMPTY);
    }
  }

  @Override
  public BaseResponse isLogin(String token) {
    try {
      JwtHelper.verifyToken(token);
      return BaseResponse.builder().code(SC_MOVED_PERMANENTLY).msg(IS_LOGIN.getMessage()).build();
    } catch (Exception e) {
      return null;
    }
  }

  //  @Override
  //  @Transactional(rollbackFor = Exception.class)
  //  public AccountResponse resetPassword(String token, String password) {
  //    MongoReset mongoReset =
  //        Optional.ofNullable(passwordMongodbDao.findByToken(token))
  //            .orElseThrow(() -> new AccountException(AccountEnum.RESET_TIME_EXPIRE));
  //    Account account =
  //        Optional.ofNullable(accountDao.findByEmailAndActiveTrue(mongoReset.getEmail()))
  //            .map(e -> e.toBuilder().password(password).build())
  //            .orElseThrow(() -> new AccountException(AccountEnum.ACCOUNT_IS_EMPTY));
  //    return new AccountResponse(
  //        Optional.of(accountDao.save(account))
  //            .map(e -> true)
  //            .orElseThrow(() -> new CustomException(ResultCode.FAILURE)));
  //  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#updateVO.getId().concat(':findById:')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
      })
  public AccountResponse updateAvatar(
      String token, UpdateVO updateVO, MultipartFile multipartFile) {
    CompletableFuture<String> avatarFuture;
    CompletableFuture<String> uploadFuture;

    try {
      avatarFuture =
          CompletableFuture.supplyAsync(
                  () ->
                      Optional.ofNullable(multipartFile)
                          .map(
                              e ->
                                  AvatarHelper.generaNewOssAvatarNameForAccount(
                                      updateVO.getId(), e.getOriginalFilename()))
                          .orElseThrow(() -> new CustomException(PARAM_MISS)),
                  appConfiguration.getAsyncExecutor())
              .exceptionally(
                  e -> {
                    log.error(
                        METHOD_ERROR_TEMPLATE,
                        Thread.currentThread().getName(),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        e.getMessage());
                    throw new CustomException(e.getMessage());
                  });
    } catch (Exception e) {
      throw new AccountException(UPLOAD_AVATAR_FAIL);
    }

    try {
      uploadFuture =
          CompletableFuture.supplyAsync(
                  () -> {
                    try {
                      return ossHelper.upload(
                          ossConfiguration.getUrl(),
                          ossConfiguration.getBucket(),
                          avatarFuture.join(),
                          multipartFile.getInputStream());
                    } catch (IOException e) {
                      throw new CustomException(e.getMessage());
                    }
                  },
                  appConfiguration.getAsyncExecutor())
              .exceptionally(
                  e -> {
                    log.error(
                        METHOD_ERROR_TEMPLATE,
                        Thread.currentThread().getName(),
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        e.getMessage());
                    throw new CustomException(e.getMessage());
                  });
    } catch (Exception e) {
      throw new AccountException(UPLOAD_AVATAR_FAIL);
    }

    String key = UPDATE_AVATAR.concat(updateVO.getId());
    try {
      locker.lock(key, 10L);
      accountDao.update(uploadFuture.join(), updateVO.getId());
      return new AccountResponse(UPDATE_AVATAR_SUCCESS);
    } catch (Exception e) {
      throw new AccountException(UPDATE_AVATAR_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#updateAccountRoleVO.getAccountId().concat(':findById:')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
      })
  public AccountResponse updateAccountRole(String token, UpdateAccountRoleVO updateAccountRoleVO) {
    UpdateAccountRoleVO vo =
        Optional.ofNullable(updateAccountRoleVO)
            .orElseThrow(() -> new CustomException(PARAM_MISSING));
    String key = LOCK_UPDATE_ACCOUNT_ROLE.concat(vo.getAccountId());
    try {
      locker.lock(key, 10L);
      AccountRoleMapping mapping =
          Optional.ofNullable(accountRoleMappingDao.findByAccountId(vo.getAccountId()))
              .map(e -> accountRoleMappingDao.save(e.toBuilder().roleId(vo.getRoleId()).build()))
              .orElseThrow(() -> new AccountException(WITHOUT_RES));
      return new AccountResponse(UPDATE_ACCOUNT_ROLE_SUCCESS, mapping);
    } catch (Exception e) {
      throw new AccountException(UPDATE_ACCOUNT_ROLE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public AccountResponse isUnique(String type, String data) {
    AccountResponse accountResponse = new AccountResponse();
    switch (type) {
      case "email":
        accountResponse.setData(!emailValidator(data));
        break;
      case "phone":
        accountResponse.setData(!phoneNumberValidator(data));
        break;
      case "name":
        accountResponse.setData(!nameValidator(data));
        break;
      default:
        throw new CustomException(PARAM_TYPE_ERROR);
    }
    return accountResponse;
  }

  @Override
  public AccountResponse test() {
    return new AccountResponse();
  }

  protected List<EmployeeDto> getEmployeeDtoList(Stream<Account> e) {
    return e.map(
            i ->
                EmployeeDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .email(i.getEmail())
                    .phoneNumber(i.getPhoneNumber())
                    .avatarUrl(i.getAvatarUrl())
                    .gender(i.getGender())
                    .isActive(i.isActive())
                    .isAdmin(i.isAdmin())
                    .createTime(i.getCreateTime())
                    .role(this.getRoleListCompletableFuture(i.getId()).join())
                    .permissionList(this.getPermissionListCompletableFuture(i.getId()).join())
                    .build())
        .collect(Collectors.toList());
  }

  protected List<SimpleAccountDto> getSimpleAccountDtoList(Stream<Account> e) {
    return e.map(
            i ->
                SimpleAccountDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .avatarUrl(i.getAvatarUrl())
                    .isAdmin(i.isAdmin())
                    .build())
        .collect(Collectors.toList());
  }

  protected List<EmployeeDto> getEmployeeDtoListWithInactive(Stream<MongoEmployee> e) {
    return e.map(
            i ->
                EmployeeDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .email(i.getEmail())
                    .phoneNumber(i.getPhoneNumber())
                    .avatarUrl(i.getAvatarUrl())
                    .gender(i.getGender())
                    .isActive(false)
                    .isAdmin(i.isAdmin())
                    .createTime(i.getCreateTime())
                    .build())
        .collect(Collectors.toList());
  }

  protected boolean nameValidator(String data) {
    return NAME_VALIDATOR.op(
            accountDao,
            accountMongodbDao,
            mailMongodbDao,
            employeeMongodbDao,
            data,
            appConfiguration)
        || this.isHasAdminStr(data);
  }

  protected boolean phoneNumberValidator(String data) {
    return PHONE_NUMBER_VALIDATOR.op(
        accountDao, accountMongodbDao, mailMongodbDao, employeeMongodbDao, data, appConfiguration);
  }

  protected boolean emailValidator(String data) {
    return EMAIL_VALIDATOR.op(
        accountDao, accountMongodbDao, mailMongodbDao, employeeMongodbDao, data, appConfiguration);
  }

  protected String getAccountIdByToken(String token) {
    return Optional.ofNullable(token)
        .map(JwtHelper::verifyToken)
        .map(e -> e.get(CLAIM_ID).asString())
        .orElseThrow(() -> new CustomException(PARAM_MISSING));
  }

  protected AccountResponse getAccountDto(
      CompletableFuture<List<String>> roleListFuture,
      CompletableFuture<List<String>> permissionListFuture,
      Account e) {
    AccountDT accountDto = AccountDT.builder().build();
    BeanUtils.copyProperties(e, accountDto);
    return new AccountResponse(
        accountDto
            .toBuilder()
            .role(roleListFuture.join())
            .permissionList(permissionListFuture.join())
            .build());
  }

  protected CompletableFuture<List<String>> getPermissionListCompletableFuture(String id) {
    return CompletableFuture.supplyAsync(
            () -> permissionService.findPermissionListByAccountId(id),
            appConfiguration.getAsyncExecutor())
        .thenApply(e -> e.stream().map(Permission::getPerm).collect(Collectors.toList()));
  }

  protected CompletableFuture<List<String>> getRoleListCompletableFuture(String id) {
    return CompletableFuture.supplyAsync(
            () -> roleService.findRoleByAccountId(id), appConfiguration.getAsyncExecutor())
        .thenApply(e -> e.stream().map(Role::getName).collect(Collectors.toList()));
  }

  protected boolean isHasAdminStr(String name) {
    return name.equalsIgnoreCase(ROLE_ADMIN);
  }
}
