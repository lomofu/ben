package com.ben.company.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/1/27 20:15
 */
@Getter
@AllArgsConstructor
public enum ProjectEnum {
  PROJECT_IS_EMPTY("项目为空", NOT_FOUND),

  WITHOUT_RES("没有结果", NOT_FOUND),

  PROJECT_CREATE_SUCCESS("新项目创建成功", SUCCESS),

  PROJECT_CREATE_FAIL("项目创建失败,请稍后重新尝试", FAILURE),

  PROJECT_NAME_EXISTED("项目名称已经存在", FAILURE),

  PROJECT_UPDATE_SUCCESS("项目信息修改成功", FAILURE),

  PROJECT_UPDATE_FAIL("项目信息修改失败,请稍后重新尝试", FAILURE),

  PROJECT_DELETE_SUCCESS("项目删除成功", SUCCESS),

  PROJECT_DELETE_FAIL("项目删除失败,请稍后重新尝试", FAILURE),

  PROJECT_LIST_DELETE_SUCCESS("批量项目删除成功", SUCCESS),

  PROJECT_LIST_DELETE_FAIL("批量删除项目失败,请稍后重新尝试", FAILURE),

  PROJECT_CHANGE_TEAM_SUCCESS("项目更换团队成功", SUCCESS),

  PROJECT_CHANGE_TEAM_FAIL("项目更换团队失败", FAILURE),

  MAPPING_DELETE_FAIL("项目映射删除失败", FAILURE),

  EMPLOYEE_LIST_EMPTY("员工列表不能为空", FAILURE),

  SOMEONE_HAS_EXIST_ON_THE_LIST("有员工已经存在在这个团队中", FAILURE),

  EMPLOYEE_ADD_PROJECT_SUCCESS("员工加入项目成功", SUCCESS),

  EMPLOYEE_ADD_PROJECT_FAIL("员工加入项目失败", FAILURE),

  PROJECT_REMOVE_FAIL("移除当前团队成功", FAILURE),

  PROJECT_REMOVE_SUCCESS("移除当前团队成功", SUCCESS);

  final String message;

  final ResultCode resultCode;
}
