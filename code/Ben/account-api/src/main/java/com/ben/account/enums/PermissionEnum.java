package com.ben.account.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/3/15 17:47
 */
@Getter
@AllArgsConstructor
public enum PermissionEnum {
  WITHOUT_RES("没有任何权限", NOT_FOUND),

  SAVE_PERM_SUCCESS("添加权限成功", SUCCESS),

  SAVE_PERM_FAIL("添加权限失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
