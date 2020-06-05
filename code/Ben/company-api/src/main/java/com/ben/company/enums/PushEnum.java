package com.ben.company.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * @author lomofu
 * @date 2020/3/22 02:36
 */
@Getter
@AllArgsConstructor
public enum PushEnum {
  MESSAGE_NOT_FOUND("没有找到此公告", NOT_FOUND),

  PUSH_NOTIFICATION_SUCCESS("公告推送成功", SUCCESS),

  PUSH_NOTIFICATION_FAIL("公告推送失败", FAILURE),

  NO_SCHEDULE("本周无行程安排", FAILURE),

  PUSH_SCHEDULE_SUCCESS("推送行程成功", SUCCESS),

  PUSH_SCHEDULE_FAIL("推送行程失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
