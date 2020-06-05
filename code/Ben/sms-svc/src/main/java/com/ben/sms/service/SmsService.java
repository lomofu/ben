package com.ben.sms.service;

import com.ben.bot.dto.mail.ScheduleDto;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/8 20:32
 */
public interface SmsService {
  void sendPhoneCode(String phoneNumber, String code);

  void sendLoginToMuch(String phoneNumber);

  void sendNotification(List<String> phoneNumber, String templateParam);

  void sendSchedule(ScheduleDto scheduleDto);
}
