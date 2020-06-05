package com.ben.sms.rocketmq;

import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.sms.service.SmsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ben.bot.constant.SmsConstant.SEND_SMS_WITH_SCHEDULE;

/**
 * @author lomofu
 * @date 2020/3/25 16:37
 */
@Component
@RocketMQMessageListener(
    topic = SEND_SMS_WITH_SCHEDULE,
    consumerGroup = SendSMSWithScheduleListener.BOT_SMS_SCHEDULE_CONSUMER)
public class SendSMSWithScheduleListener implements RocketMQListener<ScheduleDto> {
  public static final String BOT_SMS_SCHEDULE_CONSUMER = "bot-sms-schedule-consumer";

  @Resource private SmsService smsService;

  @Override
  public void onMessage(ScheduleDto scheduleDto) {
    smsService.sendSchedule(scheduleDto);
  }
}
