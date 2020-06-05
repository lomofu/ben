package com.ben.company.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/2/14 15:14
 */
@Getter
@AllArgsConstructor
public enum JobEnum {
  JOB_IS_EMPTY("任务为空", NOT_FOUND),

  WITHOUT_RES("没有结果", NOT_FOUND),

  JOB_IS_EXIST("相同任务已经存在", FAILURE),

  JOB_CREATED_SUCCESS("新任务创建成功", SUCCESS),

  JOB_CREATED_FAIL("新任务创建失败", FAILURE),

  JOB_IS_NOT_MODIFY("没有做任何修改", FAILURE),

  JOB_UPDATE_SUCCESS("任务修改成功", SUCCESS),

  JOB_UPDATE_FAIL("任务修改失败", FAILURE),

  JOB_DELETE_SUCCESS("任务删除成功", SUCCESS),

  JOB_DELETE_FAIL("任务删除失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
