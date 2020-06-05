package com.ben.account.service;

import com.ben.account.domain.AccountRoleMapping;
import com.ben.account.domain.Role;
import com.ben.account.vo.AccountResponse;
import com.ben.account.vo.NewRoleVO;
import com.ben.account.vo.UpdateRoleVO;
import com.ben.common.domain.PageFilter;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/15 17:17
 */
public interface RoleService {
  List<Role> findRoleByAccountId(String accountId);

  AccountResponse findSimpleRoleListByCompanyId(String companyId);

  AccountResponse findRoleListByCompanyId(PageFilter<String> pageFilter);

  AccountResponse createNewRole(NewRoleVO newRoleVO);

  AccountResponse updateRole(UpdateRoleVO updateRoleVO);

  AccountResponse deleteRole(String id);

  AccountResponse deleteRolesByRoleList(List<String> roleListId);

  AccountResponse updateRolePerms(String roleId, List<String> permIdList);

  AccountRoleMapping addRoleForAccountByRoleName(String roleName, String accountId);
}
