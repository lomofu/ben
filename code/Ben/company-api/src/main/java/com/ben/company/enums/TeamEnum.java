package com.ben.company.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/1/26 22:59
 */
@Getter
@AllArgsConstructor
public enum TeamEnum {
  WITHOUT_RES("没有结果", NOT_FOUND),

  TEAM_IS_EMPTY("团队不存在", NOT_FOUND),

  TEAM_CREATE_SUCCESS("新团队创建成功", SUCCESS),

  TEAM_CREATE_FAIL("新团队创建失败,请稍后重新尝试", FAILURE),

  TEAM_NAME_EXISTED("团队名称已经存在", FAILURE),

  TEAM_UPDATE_SUCCESS("团队信息更新成功", SUCCESS),

  TEAM_UPDATE_FAIL("团队信息更新失败,请稍后重新尝试", FAILURE),

  TEAM_DELETE_SUCCESS("团队删除成功", SUCCESS),

  TEAM_DELETE_FAIL("团队删除失败,请稍后重新尝试", FAILURE),

  TEAM_LIST_DELETE_SUCCESS("批量删除团队成功", SUCCESS),

  TEAM_LIST_DELETE_FAIL("批量删除团队失败,请稍后重新尝试", FAILURE),

  MAPPING_INSERT_FAIL("关系映射插入失败", FAILURE),

  MAPPING_DELETE_FAIL("关系映射删除失败", FAILURE),

  EMPLOYEE_LIST_EMPTY("员工列表不能为空", FAILURE),

  SOMEONE_HAS_EXIST_ON_THE_LIST("有员工已经存在在这个团队中", FAILURE),

  EMPLOYEE_ADD_TEAM_SUCCESS("员工加入团队成功", SUCCESS),

  EMPLOYEE_ADD_TEAM_FAIL("员工加入团队失败", FAILURE),

  TEAM_REMOVE_SUCCESS("移除当前团队成功", SUCCESS),

  TEAM_REMOVE_FAIL("移除当前团队失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
