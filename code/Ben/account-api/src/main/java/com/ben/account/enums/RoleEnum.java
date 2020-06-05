package com.ben.account.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/3/15 17:24
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
  WITHOUT_RES("未找到该角色", NOT_FOUND),

  MISS_PARAM("缺少角色对象", FAILURE),

  ROLE_HAS_EXIST("该角色已经存在", FAILURE),

  INSERT_NEW_ROLE_FAIL("创建新角色失败", FAILURE),

  INSERT_NEW_ROLE_SUCCESS("创建新角色成功", SUCCESS),

  ADD_ROLE_FOR_ACCOUNT_SUCCESS("添加角色成功", SUCCESS),

  ADD_ROLE_FOR_ACCOUNT_FAIL("添加角色失败", FAILURE),

  UPDATE_ROLE_SUCCESS("更新角色信息成功", SUCCESS),

  UPDATE_ROLE_FAIL("更新角色信息失败", FAILURE),

  UPDATE_ACCOUNT_ROLE_SUCCESS("更新账户角色成功", SUCCESS),

  UPDATE_ACCOUNT_ROLE_FAIL("更新账户角色失败", SUCCESS),

  DELETE_ROLE_SUCCESS("删除角色成功", SUCCESS),

  DELETE_ROLE_FAIL("删除角色失败", FAILURE),

  DELETE_ACCOUNT_ROLE_SUCCESS("移除账户角色成功", SUCCESS),

  DELETE_ACCOUNT_ROLE_FAIL("移除账户角色失败", FAILURE),

  UPDATE_ROLE_PERM_SUCCESS("更改角色权限成功", SUCCESS),

  UPDATE_ROLE_PERM_FAIL("更改角色权限失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
