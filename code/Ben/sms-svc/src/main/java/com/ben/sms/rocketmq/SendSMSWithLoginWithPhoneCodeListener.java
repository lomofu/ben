package com.ben.sms.rocketmq;

import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.sms.service.SmsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ben.sms.rocketmq.SendSMSWithLoginWithPhoneCodeListener.BOT_LOGIN_WITH_PHONE_CODE_CONSUMER;
import static com.ben.sms.rocketmq.SendSMSWithLoginWithPhoneCodeListener.SEND_SMS_WITH_LOGIN_WITH_PHONE_CODE;

/**
 * @author lomofu
 * @date 2020/3/8 20:14
 */
@Component
@RocketMQMessageListener(
    topic = SEND_SMS_WITH_LOGIN_WITH_PHONE_CODE,
    consumerGroup = BOT_LOGIN_WITH_PHONE_CODE_CONSUMER)
public class SendSMSWithLoginWithPhoneCodeListener
    implements RocketMQListener<LoginWithPhoneCodeDto> {
  public static final String SEND_SMS_WITH_LOGIN_WITH_PHONE_CODE = "sendSMSWithLoginWithPhoneCode";
  public static final String BOT_LOGIN_WITH_PHONE_CODE_CONSUMER =
      "bot-sms-login-with-phoneCode-consumer";

  @Resource private SmsService smsService;

  @Override
  public void onMessage(LoginWithPhoneCodeDto loginWithPhoneCodeDto) {
    smsService.sendPhoneCode(
        loginWithPhoneCodeDto.getPhoneNumber(), loginWithPhoneCodeDto.getCode());
  }
}
