package com.ben.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.FAILURE;
import static com.ben.common.enums.ResultCode.SUCCESS;

/**
 * @author lomofu
 * @date 2020/3/4 04:54
 */
@Getter
@AllArgsConstructor
public enum BotEnum {
  SEND_NOTIFICATION_SUCCESS(SUCCESS.getCode(), "公告推送成功"),

  SEND_NOTIFICATION_ERROR(FAILURE.getCode(), "公告推送失败"),

  SEND_SCHEDULE_SUCCESS(SUCCESS.getCode(), "排班推送成功"),

  SEND_SCHEDULE_ERROR(FAILURE.getCode(), "排班推送失败");

  final int code;

  final String message;
}
