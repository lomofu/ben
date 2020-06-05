package com.ben.account.service;

import com.ben.account.domain.Permission;
import com.ben.account.vo.AccountResponse;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/15 17:32
 */
public interface PermissionService {
  List<Permission> findPermissionListByRoleId(String roleId);

  List<Permission> findPermissionListByAccountId(String accountId);

  List<Permission> findAll();

  AccountResponse getPermListByRoleId(String roleId);

  AccountResponse getAllPermList();

  AccountResponse getSettingMenu(String accountId);
}
