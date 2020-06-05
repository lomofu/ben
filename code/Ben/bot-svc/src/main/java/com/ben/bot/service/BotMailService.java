package com.ben.bot.service;

import com.ben.bot.dto.mail.*;
import com.ben.bot.vo.BotResponse;

/**
 * @author lomofu
 * @date 2020/3/4 05:35
 */
public interface BotMailService {
  BotResponse sendEmailWithCreateNewAdmin(CreateNewAdminDto createNewAdminDto);

  BotResponse sendEmailWithCreateNewAdminSuccess(
      CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto);

  BotResponse sendEmailWithCreateNewEmployee(CreateNewEmployeeDto createNewEmployeeDto);

  BotResponse sendEmailWithCreateNewEmployeeSuccess(
      CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto);

  BotResponse sendEmailWithLoginToMuch(LoginToMuchDto loginToMuchDto);

  BotResponse sendEmailWithNotification(NotificationDto notificationDto);

  BotResponse sendEmailWithSchedule(ScheduleDto scheduleDto);
}
