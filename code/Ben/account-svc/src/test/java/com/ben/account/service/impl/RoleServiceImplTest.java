package com.ben.account.service.impl;

import com.ben.account.config.AppConfiguration;
import com.ben.account.dao.AccountDao;
import com.ben.account.dao.AccountRoleMappingDao;
import com.ben.account.dao.RoleDao;
import com.ben.account.dao.RolePermMappingDao;
import com.ben.account.domain.Role;
import com.ben.account.factory.MockBeanFactory;
import com.ben.account.vo.NewRoleVO;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.ben.account.factory.MockBeanFactory.buildRole;
import static com.ben.account.factory.MockBeanFactory.buildRoleVO;

public class RoleServiceImplTest {
  public static final String ACCOUNT_ID = "ff8080816fecb64a016fecbd23f70001";

  public static final String COMPANY_ID = "6f11593ce2cc4cf3892c292b6cb45fde";

  @Tested(fullyInitialized = true)
  private RoleServiceImpl roleService;

  @Injectable private RoleDao roleDao;

  @Injectable private AccountDao accountDao;

  @Injectable private RolePermMappingDao rolePermMappingDao;

  @Injectable private AccountRoleMappingDao accountRoleMappingDao;

  @Injectable private RedissonDistributedLocker locker;

  @Injectable private RedisHelper redisHelper;

  @Injectable private AppConfiguration appConfiguration;

  @Test
  public void should_return_role_success_when_find_role_by_accountId() {
    Assertions.assertNotNull(roleDao);
    mockFindRoleByAccountId();
    Assertions.assertNotNull(roleService.findRoleByAccountId(ACCOUNT_ID));
    Assertions.assertEquals(1, roleService.findRoleByAccountId(ACCOUNT_ID).size());
  }

  @Test
  public void should_return_list_when_find_simple_role_list_by_companyId() {
    Assertions.assertNotNull(roleDao);
    mockFindRoleListByCompanyId();
    Assertions.assertNotNull(roleService.findSimpleRoleListByCompanyId(COMPANY_ID).getData());
    Assertions.assertEquals(200, roleService.findSimpleRoleListByCompanyId(COMPANY_ID).getCode());
  }

  @Test
  public void should_return_exception_when_create_new_role() {
    Assertions.assertNotNull(roleDao);
    Assertions.assertNotNull(locker);
    NewRoleVO newRoleVO = buildRoleVO();
    Role role =
        Role.builder()
            .createBy(newRoleVO.getCreateBy())
            .active(true)
            .description(newRoleVO.getDescription())
            .name(newRoleVO.getName())
            .build();
    mockSaveRole(role);
    Assertions.assertNotNull(roleService.createNewRole(newRoleVO));
    Assertions.assertEquals(roleService.createNewRole(newRoleVO).getCode(), 200);
  }

  protected void mockSaveRole(Role role) {
    new Expectations(role) {
      {
        roleDao.save(role);
        result = new Role();
      }
    };
  }

  protected void mockFindRoleListByCompanyId() {
    new Expectations() {
      {
        roleDao.findRoleListByCompanyId(anyString);
        result = MockBeanFactory.buildRoleList();
      }
    };
  }

  protected void mockFindRoleByAccountId() {
    new Expectations() {
      {
        roleDao.findRoleByAccountId(anyString);
        result = buildRole();
      }
    };
  }
}
