package com.ben.account.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * AccountEnum Enum
 *
 * @author lomofu
 */
@Getter
@AllArgsConstructor
public enum AccountEnum {
  LOGIN_SUCCESS("登陆成功", SUCCESS),

  ACCOUNT_IS_EMPTY("账户不存在", NOT_FOUND),

  IS_LOGIN("账户在登陆状态", SUCCESS),

  INPUT_ACCOUNT_IS_ILLEGAL("输入账号格式不正确", FAILURE),

  ACCOUNT_EXIST("账号已存在", FAILURE),

  WITHOUT_RES("没有结果", NOT_FOUND),

  ACCOUNT_TIME_EXPIRE("账号已过期", FAILURE),

  ACCOUNT_CREATE_SUCCESS("账户创建成功", SUCCESS),

  ACCOUNT_CREATE_FAIL("账户创建失败", FAILURE),

  ACCOUNT_UPDATE_SUCCESS("账户信息更新成功", SUCCESS),

  ACCOUNT_UPDATE_FAIL("账号信息更新失败", FAILURE),

  ACCOUNT_DELETE_SUCCESS("账户删除成功", SUCCESS),

  ACCOUNT_DELETE_FAIL("账户删除失败", FAILURE),

  TEMP_ACCOUNT_DELETE_SUCCESS("未激活账号删除成功", SUCCESS),

  TEMP_ACCOUNT_DELETE_FAIL("未激活账号删除失败", FAILURE),

  TEMP_ACCOUNT_LIST_DELETE_SUCCESS("批量删除未激活账号成功", SUCCESS),

  TEMP_ACCOUNT_LIST_DELETE_FAIL("批量删除未激活账号失败", FAILURE),

  TEMP_ACCOUNT_REMOVE_TEAM_SUCCESS("未激活员工移除成功", SUCCESS),

  TEAM_ACCOUNT_REMOVE_TEAM_FAIL("未激活员工移除失败", FAILURE),

  ACCOUNT_LIST_DELETE_SUCCESS("批量删除账户成功", SUCCESS),

  ACCOUNT_LIST_DELETE_FAIL("批量删除账户失败", FAILURE),

  ERROR_ACCOUNT_OR_PASSWORD("账号或者密码错误", FAILURE),

  RESET_TIME_EXPIRE("重置密码时间过期", FAILURE),

  CREATE_EMPLOYEE_SUCCESS("创建员工账号成功", SUCCESS),

  CREATE_EMPLOYEE_FAIL("创建员工账号失败", FAILURE),

  TOO_MUCH_TIME_LOGIN("你的账号因为登陆失败次数过多已被锁定,请与30分分钟后重试", FAILURE),

  SMS_SEND_SUCCESS("验证码发送成功,请查看您的手机短信", SUCCESS),

  SMS_SEND_FAIL("验证码发送失败,请稍后重试", SUCCESS),

  SMS_HAS_EXIST("操作过于频繁,请稍后再试", FAILURE),

  SMS_CODE_EXPIRED("验证码已过期,请重新获取", FAILURE),

  SMS_CODE_LOGIN_FAIL("登录失败,手机或验证码错误", FAILURE),

  UPLOAD_AVATAR_FAIL("上传头像失败", FAILURE),

  UPDATE_AVATAR_SUCCESS("头像更新成功", SUCCESS),

  UPDATE_AVATAR_FAIL("头像更新失败", FAILURE),

  UPDATE_ACCOUNT_ROLE_SUCCESS("更新账户角色成功", SUCCESS),

  UPDATE_ACCOUNT_ROLE_FAIL("更新账户角色失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
