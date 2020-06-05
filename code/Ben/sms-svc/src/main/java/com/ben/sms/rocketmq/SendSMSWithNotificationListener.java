package com.ben.sms.rocketmq;

import com.ben.bot.dto.sms.NotificationDto;
import com.ben.sms.service.SmsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ben.bot.constant.SmsConstant.SEND_SMS_WITH_NOTIFICATION;
import static com.ben.sms.rocketmq.SendSMSWithNotificationListener.BOT_SMS_NOTIFICATION_CONSUMER;

/**
 * @author lomofu
 * @date 2020/3/24 21:11
 */
@Component
@RocketMQMessageListener(
    topic = SEND_SMS_WITH_NOTIFICATION,
    consumerGroup = BOT_SMS_NOTIFICATION_CONSUMER)
public class SendSMSWithNotificationListener implements RocketMQListener<NotificationDto> {
  public static final String BOT_SMS_NOTIFICATION_CONSUMER = "bot-sms-notification-consumer";

  @Resource private SmsService smsService;

  @Override
  public void onMessage(NotificationDto notificationDto) {
    smsService.sendNotification(notificationDto.getList(), notificationDto.getTitle());
  }
}
