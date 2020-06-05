package com.ben.account.service.impl;

import com.ben.account.config.AppConfiguration;
import com.ben.account.config.OssConfiguration;
import com.ben.account.dao.*;
import com.ben.account.exception.AccountException;
import com.ben.account.service.PermissionService;
import com.ben.account.service.RoleService;
import com.ben.bot.client.BotMailClient;
import com.ben.bot.client.BotSmsClient;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.OssHelper;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.client.CompanyClient;
import com.ben.company.vo.company.CompanyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.ben.account.enums.AccountEnum.ACCOUNT_DELETE_FAIL;
import static com.ben.account.enums.AccountEnum.TEMP_ACCOUNT_DELETE_FAIL;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {
  public static final String EMPLOYEE_ID = "8aaa81787145312d0171454153540002";

  public static final String ACCOUNT_ID = "8aaa816d7141c5d3017141ca4bf30000";

  @Tested(fullyInitialized = true)
  private AccountServiceImpl accountService;

  @Injectable private AccountDao accountDao;

  @Injectable private AccountRoleMappingDao accountRoleMappingDao;

  @Injectable private AccountMongodbDao accountMongodbDao;

  @Injectable private MailMongodbDao mailMongodbDao;

  @Injectable private PasswordMongodbDao passwordMongodbDao;

  @Injectable private EmployeeMongodbDao employeeMongodbDao;

  @Injectable private CompanyClient companyClient;

  @Injectable private BotMailClient botMailClient;

  @Injectable private BotSmsClient botSmsClient;

  @Injectable private RoleService roleService;

  @Injectable private PermissionService permissionService;

  @Injectable private RedissonDistributedLocker locker;

  @Injectable private OssHelper ossHelper;

  @Injectable private RedisHelper redisHelper;

  @Injectable private ObjectMapper objectMapper;

  @Injectable private AppConfiguration appConfiguration;

  @Injectable private OssConfiguration ossConfiguration;

  @Test
  public void should_return_success_when_delete_temp_employee() {
    assertNotNull(employeeMongodbDao);
    mockEmployeeMongodbDaoDeleteById();
    accountService.deleteTempEmployee(EMPLOYEE_ID);
    assertDoesNotThrow(() -> new AccountException(TEMP_ACCOUNT_DELETE_FAIL));
  }

  @Test
  public void should_throw_exception_when_delete_temp_employee() {
    assertNotNull(employeeMongodbDao);
    new Expectations() {
      {
        employeeMongodbDao.deleteById(anyString);
        result = new AccountException(TEMP_ACCOUNT_DELETE_FAIL);
      }
    };
    AccountException exception =
        assertThrows(AccountException.class, () -> accountService.deleteTempEmployee(EMPLOYEE_ID));
    Assertions.assertEquals(exception.getMessage(), TEMP_ACCOUNT_DELETE_FAIL.getMessage());
  }

  @Test
  public void should_return_success_when_delete_employee() {
    assertNotNull(accountDao);
    assertNotNull(companyClient);
    mockAccountDaoDeleteEmployeeAccount();
    mockCompanyClientDeleteAccountMappingForFeign();
    accountService.deleteEmployee("token", EMPLOYEE_ID);
    Assertions.assertDoesNotThrow(() -> new AccountException(ACCOUNT_DELETE_FAIL));
  }

  @Test
  public void should_throw_exception_when_delete_employee() {
    assertNotNull(accountDao);
    assertNotNull(companyClient);
    new Expectations() {
      {
        accountDao.deleteEmployeeAccount(anyString);
        result = new AccountException(ACCOUNT_DELETE_FAIL);
      }
    };
    AccountException exception =
        assertThrows(
            AccountException.class, () -> accountService.deleteEmployee("token", EMPLOYEE_ID));
    assertEquals(exception.getMessage(), ACCOUNT_DELETE_FAIL.getMessage());
  }

  @Test
  public void should_return_token_error_when_find_by_id() {
    assertNotNull(permissionService);
    assertNotNull(accountDao);
    Throwable exception =
        assertThrows(CustomException.class, () -> accountService.findById("token", ACCOUNT_ID));
    assertEquals(exception.getMessage(), "凭证过期！");
  }

  protected void mockCompanyClientDeleteAccountMappingForFeign() {
    new Expectations() {
      {
        companyClient.deleteAccountMappingForFeign(anyString, anyString, anyString);
        result = new CompanyResponse();
      }
    };
  }

  protected Expectations mockAccountDaoDeleteEmployeeAccount() {
    return new Expectations() {
      {
        accountDao.deleteEmployeeAccount(anyString);
        result = 1;
      }
    };
  }

  protected void mockEmployeeMongodbDaoDeleteById() {
    new Expectations() {
      {
        employeeMongodbDao.deleteById(anyString);
      }
    };
  }
}
