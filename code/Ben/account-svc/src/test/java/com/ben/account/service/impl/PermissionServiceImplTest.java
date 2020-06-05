package com.ben.account.service.impl;

import com.ben.account.dao.PermDao;
import com.ben.account.domain.Permission;
import com.ben.account.service.RoleService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PermissionServiceImplTest {
  public static final String ROLE_ID = "8aaa81727144560f017144600d630000";
  public static final String ACCOUNT_ID = "8aaa816d7141c5d3017141ca4bf30000";

  @Tested private PermissionServiceImpl permissionService;

  @Injectable private PermDao permDao;

  @Injectable private RoleService roleService;

  @Test
  public void should_return_permission_list_when_find_permission_list_by_role_id() {
    assertNotNull(permDao);
    new Expectations() {
      {
        permDao.findPermissionsByRoleId(anyString);
        result = new ArrayList<>();
      }
    };
    List<Permission> list = permissionService.findPermissionListByRoleId(ROLE_ID);
    assertNotNull(list);
  }

  @Test
  public void should_return_permission_list_when_find_permission_list_by_account_id() {
    assertNotNull(permDao);
    new Expectations() {
      {
        permDao.findPermissionsByAccountId(anyString);
        result = new ArrayList<>();
      }
    };
    List<Permission> list = permissionService.findPermissionListByAccountId(ACCOUNT_ID);
    assertNotNull(list);
  }

  @Test
  public void should_return_permission_list_when_findAll() {
    assertNotNull(permDao);
    new Expectations() {
      {
        permDao.findAll();
        result = new ArrayList<>();
      }
    };
    List<Permission> list = permissionService.findAll();
    assertNotNull(list);
  }
}
