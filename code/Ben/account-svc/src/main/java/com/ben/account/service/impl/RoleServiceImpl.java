package com.ben.account.service.impl;

import com.ben.account.config.AppConfiguration;
import com.ben.account.dao.AccountDao;
import com.ben.account.dao.AccountRoleMappingDao;
import com.ben.account.dao.RoleDao;
import com.ben.account.dao.RolePermMappingDao;
import com.ben.account.domain.Account;
import com.ben.account.domain.AccountRoleMapping;
import com.ben.account.domain.Role;
import com.ben.account.domain.RolePermMapping;
import com.ben.account.dto.RoleDto;
import com.ben.account.dto.SimpleRoleDto;
import com.ben.account.exception.RoleException;
import com.ben.account.service.RoleService;
import com.ben.account.utils.PageFilterForJpaHelper;
import com.ben.account.vo.AccountResponse;
import com.ben.account.vo.NewRoleVO;
import com.ben.account.vo.UpdateRoleVO;
import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.common.utils.UUIDHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ben.account.enums.RoleEnum.*;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;

/**
 * @author lomofu
 * @date 2020/3/15 17:18
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
  public static final String LOCK_CREATE_NEW_ROLE = "LockCreateNewRole";

  public static final String LOCK_UPDATE_ROLE = "LockUpdateRole";

  public static final String LOCK_DELETE_ROLE_LIST = "LockDeleteRoleList";

  public static final String LOCK_UPDATE_ROLE_PERMS = "LockUpdateRolePerms";

  public static final String ADD_ROLE_FOR_ACCOUNT_BY_ROLE_NAME = "addRoleForAccountByRoleName";

  @Resource private RoleDao roleDao;

  @Resource private AccountDao accountDao;

  @Resource private RolePermMappingDao rolePermMappingDao;

  @Resource private AccountRoleMappingDao accountRoleMappingDao;

  @Resource private RedissonDistributedLocker locker;

  @Resource private RedisHelper redisHelper;

  @Resource private AppConfiguration appConfiguration;

  @Override
  public List<Role> findRoleByAccountId(String accountId) {
    return Optional.ofNullable(roleDao.findRoleByAccountId(accountId))
        .orElseThrow(() -> new RoleException(WITHOUT_RES));
  }

  @Override
  public AccountResponse findSimpleRoleListByCompanyId(String companyId) {
    return Optional.ofNullable(roleDao.findRoleListByCompanyId(companyId))
        .map(
            e ->
                e.stream()
                    .map(i -> SimpleRoleDto.builder().id(i.getId()).name(i.getName()).build())
                    .collect(Collectors.toList()))
        .map(AccountResponse::new)
        .orElseThrow(() -> new RoleException(WITHOUT_RES));
  }

  @Override
  public AccountResponse findRoleListByCompanyId(PageFilter<String> pageFilter) {
    Page<Role> list;
    if (pageFilter.isSortDesc()) {
      list =
          roleDao.findRoleListByCompanyIdDesc(
              pageFilter.getData(),
              PageFilterForJpaHelper.getJpaPageRequestWithoutSort(pageFilter));
    } else {
      list =
          roleDao.findRoleListByCompanyIdAsc(
              pageFilter.getData(),
              PageFilterForJpaHelper.getJpaPageRequestWithoutSort(pageFilter));
    }
    return Optional.ofNullable(list)
        .map(
            e ->
                PageVO.<RoleDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.converseToRoleDto(e.get()))
                    .build())
        .map(AccountResponse::new)
        .orElseThrow(() -> new RoleException(WITHOUT_RES));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse createNewRole(NewRoleVO newRoleVO) {
    if (Objects.isNull(newRoleVO)) {
      throw new RoleException(MISS_PARAM);
    }
    if (isHaveRoleName(newRoleVO.getName(), newRoleVO.getCompanyId())) {
      throw new RoleException(ROLE_HAS_EXIST);
    }

    String key = LOCK_CREATE_NEW_ROLE.concat(UUIDHelper.getUUID());
    try {
      locker.lock(key, 10L);
      roleDao.save(
          Role.builder()
              .name(newRoleVO.getName())
              .description(newRoleVO.getDescription())
              .createBy(newRoleVO.getCreateBy())
              .active(true)
              .build());
      return new AccountResponse(INSERT_NEW_ROLE_SUCCESS);
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new RoleException(INSERT_NEW_ROLE_FAIL);
    } finally {
      assert locker != null;
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse updateRole(UpdateRoleVO updateRoleVO) {
    if (Objects.isNull(updateRoleVO)) {
      throw new RoleException(MISS_PARAM);
    }
    if (isHaveRoleName(updateRoleVO.getName(), updateRoleVO.getCompanyId())) {
      throw new RoleException(ROLE_HAS_EXIST);
    }

    String key = LOCK_UPDATE_ROLE.concat(updateRoleVO.getId());
    try {
      locker.lock(key, 10L);
      roleDao.save(
          Role.builder()
              .id(updateRoleVO.getId())
              .name(updateRoleVO.getName())
              .description(updateRoleVO.getDescription())
              .createBy(updateRoleVO.getCreateBy())
              .active(true)
              .build());

      CompletableFuture.runAsync(
          () -> this.updateRoleCache(updateRoleVO.getId()), appConfiguration.getAsyncExecutor());

      return new AccountResponse(UPDATE_ROLE_SUCCESS);
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new RoleException(UPDATE_ROLE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse deleteRole(String id) {
    Role role =
        roleDao
            .findByIdAndActiveTrue(id)
            .map(e -> e.toBuilder().active(false).build())
            .orElseThrow(() -> new RoleException(WITHOUT_RES));
    String key = LOCK_UPDATE_ROLE.concat(id);
    try {
      locker.lock(key, 10L);
      roleDao.save(role);

      CompletableFuture.runAsync(
          () -> this.updateRoleCache(id), appConfiguration.getAsyncExecutor());

      return new AccountResponse(DELETE_ROLE_SUCCESS);
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new RoleException(DELETE_ROLE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse deleteRolesByRoleList(List<String> roleListId) {
    String key = LOCK_DELETE_ROLE_LIST.concat(UUIDHelper.getUUID());
    try {
      locker.lock(key, 15L);
      roleListId.forEach(this::deleteRole);
      return new AccountResponse(DELETE_ROLE_SUCCESS);
    } catch (Exception e) {
      throw new RoleException(DELETE_ROLE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountResponse updateRolePerms(String roleId, List<String> permIdList) {
    String key = LOCK_UPDATE_ROLE_PERMS.concat(roleId);
    try {
      locker.lock(key, 15L);
      rolePermMappingDao.deleteByRoleId(roleId);
    } catch (Exception e) {
      throw new RoleException(UPDATE_ROLE_PERM_FAIL);
    } finally {
      locker.unlock(key);
    }

    for (String permId : permIdList) {
      try {
        locker.lock(key, 15L);
        rolePermMappingDao.save(RolePermMapping.builder().permId(permId).roleId(roleId).build());
      } catch (Exception e) {
        throw new RoleException(UPDATE_ROLE_PERM_FAIL);
      } finally {
        locker.unlock(key);
      }
    }

    CompletableFuture.runAsync(
        () -> this.updateRoleCache(roleId), appConfiguration.getAsyncExecutor());

    return new AccountResponse(UPDATE_ROLE_PERM_SUCCESS);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AccountRoleMapping addRoleForAccountByRoleName(String roleName, String accountId) {
    Role role =
        roleDao.findByNameAndActiveTrue(roleName).orElseThrow(() -> new RoleException(WITHOUT_RES));
    String key = ADD_ROLE_FOR_ACCOUNT_BY_ROLE_NAME.concat(accountId);
    try {
      locker.lock(key, 10L);
      return accountRoleMappingDao.save(
          AccountRoleMapping.builder().roleId(role.getId()).accountId(accountId).build());
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new RoleException(WITHOUT_RES);
    } finally {
      locker.unlock(key);
    }
  }

  protected List<RoleDto> converseToRoleDto(Stream<Role> roleStream) {
    return roleStream
        .map(
            e ->
                RoleDto.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .description(e.getDescription())
                    .active(e.isActive())
                    .createBy(e.getCreateBy())
                    .createName(accountDao.findById(e.getCreateBy()).map(Account::getName).get())
                    .build())
        .collect(Collectors.toList());
  }

  protected boolean isHaveRoleName(String name, String companyId) {
    return roleDao.findRoleListByCompanyId(companyId).stream()
        .anyMatch(e -> name.equals(e.getName()));
  }

  protected void updateRoleCache(String roleId) {
    List<String> accountIdList =
        accountRoleMappingDao.findByRoleId(roleId).stream()
            .map(AccountRoleMapping::getAccountId)
            .collect(Collectors.toList());
    accountIdList.forEach(e -> redisHelper.del("account-svc::".concat(e).concat(":findById")));
  }
}
