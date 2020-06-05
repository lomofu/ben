package com.ben.bot.service;

import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.bot.dto.sms.NotificationDto;
import com.ben.bot.vo.BotResponse;

/**
 * @author lomofu
 * @date 2020/3/8 20:08
 */
public interface BotSmsService {
  BotResponse sendSMSWithLoginWithPhoneCode(LoginWithPhoneCodeDto loginWithPhoneCodeDto);

  BotResponse sendSMSWithLoginToMuch(String phoneNumber);

  BotResponse sendSMSWithNotification(NotificationDto notificationDto);

  BotResponse sendSMSWithSchedule(ScheduleDto scheduleDto);
}
