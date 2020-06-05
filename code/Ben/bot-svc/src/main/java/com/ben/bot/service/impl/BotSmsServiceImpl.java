package com.ben.bot.service.impl;

import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.bot.dto.sms.NotificationDto;
import com.ben.bot.service.BotSmsService;
import com.ben.bot.vo.BotResponse;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.ben.bot.constant.SmsConstant.*;

/**
 * @author lomofu
 * @date 2020/3/8 20:09
 */
@Service
public class BotSmsServiceImpl implements BotSmsService {
  @Resource private RocketMQTemplate rocketMQTemplate;

  @Override
  public BotResponse sendSMSWithLoginWithPhoneCode(LoginWithPhoneCodeDto loginWithPhoneCodeDto) {
    rocketMQTemplate.convertAndSend(SEND_SMS_WITH_LOGIN_WITH_PHONE_CODE, loginWithPhoneCodeDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendSMSWithLoginToMuch(String phoneNumber) {
    rocketMQTemplate.convertAndSend(SEND_SMS_WITH_LOGIN_TO_MUCH, phoneNumber);
    return new BotResponse();
  }

  @Override
  public BotResponse sendSMSWithNotification(NotificationDto notificationDto) {
    rocketMQTemplate.convertAndSend(SEND_SMS_WITH_NOTIFICATION, notificationDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendSMSWithSchedule(ScheduleDto scheduleDto) {
    rocketMQTemplate.convertAndSend(SEND_SMS_WITH_SCHEDULE, scheduleDto);
    return new BotResponse();
  }
}
