package com.ben.sms.rocketmq;

import com.ben.sms.service.SmsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ben.bot.constant.SmsConstant.SEND_SMS_WITH_LOGIN_TO_MUCH;
import static com.ben.sms.rocketmq.SendSMSWithLoginToMuchListener.BOT_SMS_LOGIN_TO_MUCH_CONSUMER;

/**
 * @author lomofu
 * @date 2020/3/10 14:22
 */
@Component
@RocketMQMessageListener(
    topic = SEND_SMS_WITH_LOGIN_TO_MUCH,
    consumerGroup = BOT_SMS_LOGIN_TO_MUCH_CONSUMER)
public class SendSMSWithLoginToMuchListener implements RocketMQListener<String> {
  public static final String BOT_SMS_LOGIN_TO_MUCH_CONSUMER = "bot-sms-login-to-much-consumer";

  @Resource private SmsService smsService;

  @Override
  public void onMessage(String phoneNumber) {
      smsService.sendLoginToMuch(phoneNumber);
  }
}
