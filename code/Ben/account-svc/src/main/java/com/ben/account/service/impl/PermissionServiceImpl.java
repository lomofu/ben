package com.ben.account.service.impl;

import com.ben.account.dao.PermDao;
import com.ben.account.domain.Permission;
import com.ben.account.domain.Role;
import com.ben.account.exception.PermissionException;
import com.ben.account.service.PermissionService;
import com.ben.account.service.RoleService;
import com.ben.account.vo.AccountResponse;
import com.ben.common.annotation.CacheExpire;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ben.account.enums.PermissionEnum.WITHOUT_RES;
import static com.ben.common.constant.AuthConstant.ROLE_ADMIN;
import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;

/**
 * @author lomofu
 * @date 2020/3/15 17:38
 */
@Service
@CacheConfig(cacheNames = "account-svc")
public class PermissionServiceImpl implements PermissionService {

  @Resource private PermDao permDao;

  @Resource private RoleService roleService;

  @Override
  public List<Permission> findPermissionListByRoleId(String roleId) {
    return Optional.ofNullable(permDao.findPermissionsByRoleId(roleId))
        .orElseThrow(() -> new PermissionException(WITHOUT_RES));
  }

  @Override
  public List<Permission> findPermissionListByAccountId(String accountId) {
    return Optional.ofNullable(permDao.findPermissionsByAccountId(accountId))
        .orElseThrow(() -> new PermissionException(WITHOUT_RES));
  }

  @Override
  @Cacheable(key = "'perm:'.concat(':').concat(#root.methodName)", unless = "#result == null")
  @CacheExpire(value = 24 * DEFAULT_CACHE_TIME)
  public List<Permission> findAll() {
    return Optional.of(permDao.findAll()).orElseThrow(() -> new PermissionException(WITHOUT_RES));
  }

  @Override
  public AccountResponse getPermListByRoleId(String roleId) {
    return Optional.ofNullable(this.findPermissionListByRoleId(roleId))
        .map(AccountResponse::new)
        .orElseThrow(() -> new PermissionException(WITHOUT_RES));
  }

  @Override
  public AccountResponse getAllPermList() {
    return new AccountResponse(this.findAll());
  }

  @Override
  public AccountResponse getSettingMenu(String accountId) {
    List<Permission> permissionList;
    List<Role> role = roleService.findRoleByAccountId(accountId);
    if (role.stream().anyMatch(e -> e.getName().equals(ROLE_ADMIN))) {
      permissionList = this.findAll();
    } else {
      permissionList = permDao.findPermissionsByAccountId(accountId);
    }
    return Optional.ofNullable(permissionList)
        .map(
            e ->
                e.stream()
                    .filter(i -> StringUtils.hasText(i.getUrl()))
                    .collect(Collectors.toList()))
        .map(AccountResponse::new)
        .orElseThrow(() -> new PermissionException(WITHOUT_RES));
  }
}
