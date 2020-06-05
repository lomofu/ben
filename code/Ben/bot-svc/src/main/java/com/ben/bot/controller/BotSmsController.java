package com.ben.bot.controller;

import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.bot.dto.sms.LoginWithPhoneCodeDto;
import com.ben.bot.dto.sms.NotificationDto;
import com.ben.bot.service.BotSmsService;
import com.ben.bot.vo.BotResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author lomofu
 * @date 2020/3/8 20:06
 */
@RestController
@RequestMapping("/api/bot/sms")
public class BotSmsController {

  @Resource private BotSmsService botSmsService;

  @PostMapping(path = "/loginWithPhoneCode")
  public Mono<BotResponse> sendSMSWithLoginWithPhoneCode(
      @RequestBody LoginWithPhoneCodeDto loginWithPhoneCodeDto) {
    return Mono.just(botSmsService.sendSMSWithLoginWithPhoneCode(loginWithPhoneCodeDto));
  }

  @GetMapping(path = "/loginToMuch/{phoneNumber}")
  public Mono<BotResponse> sendSMSWithLoginToMuch(@PathVariable String phoneNumber) {
    return Mono.just(botSmsService.sendSMSWithLoginToMuch(phoneNumber));
  }

  @PostMapping(path = "/notification")
  public Mono<BotResponse> sendSMSWithNotification(@RequestBody NotificationDto notificationDto) {
    return Mono.just(botSmsService.sendSMSWithNotification(notificationDto));
  }

  @PostMapping(path = "/schedule")
  public Mono<BotResponse> sendSMSWithSchedule(@RequestBody ScheduleDto scheduleDto) {
    return Mono.just(botSmsService.sendSMSWithSchedule(scheduleDto));
  }
}
