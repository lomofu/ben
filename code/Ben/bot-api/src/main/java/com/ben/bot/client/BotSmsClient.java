package com.ben.bot.client;

import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.bot.dto.sms.NotificationDto;
import com.ben.bot.vo.BotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static com.ben.bot.constant.CommonConstant.SERVICE_SMS_NAME;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;

/**
 * @author lomofu
 * @date 2020/3/8 19:53
 */
@FeignClient(
    name = SERVICE_SMS_NAME,
    path = "/api/bot/sms",
    url = "${app.endpoint.bot-service-endpoint}")
public interface BotSmsClient {
  @PostMapping(path = "/loginWithPhoneCode")
  BotResponse sendSMSWithLoginWithPhoneCode(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody LoginWithPhoneCodeDto loginWithPhoneCodeDto);

  @GetMapping(path = "/loginToMuch/{phoneNumber}")
  BotResponse sendSMSWithLoginToMuch(
      @RequestHeader(AUTHORIZATION_SERVICE) String service, @PathVariable String phoneNumber);

  @PostMapping(path = "/notification")
  BotResponse sendSMSWithNotification(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody NotificationDto notificationDto);

  @PostMapping(path = "/schedule")
  BotResponse sendSMSWithSchedule(
      @RequestHeader(AUTHORIZATION_SERVICE) String service, @RequestBody ScheduleDto scheduleDto);
}
