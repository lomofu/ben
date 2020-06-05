package com.ben.bot.controller;

import com.ben.bot.dto.mail.*;
import com.ben.bot.service.BotMailService;
import com.ben.bot.vo.BotResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lomofu
 * @date 2020/3/4 20:21
 */
@RestController
@RequestMapping("/api/bot/mail")
@Valid
public class BotMailController {

  @Resource private BotMailService botMailService;

  @PostMapping(path = "/createNewAdmin")
  public Mono<BotResponse> sendEmailWithCreateNewAdmin(
      @RequestBody @Valid CreateNewAdminDto createNewAdminDto) {
    return Mono.just(botMailService.sendEmailWithCreateNewAdmin(createNewAdminDto));
  }

  @PostMapping(path = "/createNewAdminSuccess")
  public Mono<BotResponse> sendEmailWithCreateNewAdminSuccess(
      @RequestBody @Valid CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto) {
    return Mono.just(
        botMailService.sendEmailWithCreateNewAdminSuccess(createNewEmployeeSuccessDto));
  }

  @PostMapping(path = "/createNewEmployee")
  public Mono<BotResponse> sendEmailWithCreateNewEmployee(
      @RequestBody @Valid CreateNewEmployeeDto createNewEmployeeDto) {
    return Mono.just(botMailService.sendEmailWithCreateNewEmployee(createNewEmployeeDto));
  }

  @PostMapping(path = "/createNewEmployeeSuccess")
  public Mono<BotResponse> sendEmailWithCreateNewEmployeeSuccess(
      @RequestBody @Valid CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto) {
    return Mono.just(
        botMailService.sendEmailWithCreateNewEmployeeSuccess(createNewEmployeeSuccessDto));
  }

  @PostMapping(path = "/loginToMuch")
  public Mono<BotResponse> sendEmailWithLoginToMuch(
      @RequestBody @Valid LoginToMuchDto loginToMuchDto) {
    return Mono.just(botMailService.sendEmailWithLoginToMuch(loginToMuchDto));
  }

  @PostMapping(path = "/notification")
  public Mono<BotResponse> sendEmailWithNotification(@RequestBody NotificationDto notificationDto) {
    return Mono.just(botMailService.sendEmailWithNotification(notificationDto));
  }

  @PostMapping(path = "/schedule")
  public Mono<BotResponse> sendEmailWithSchedule(@RequestBody ScheduleDto scheduleDto) {
    return Mono.just(botMailService.sendEmailWithSchedule(scheduleDto));
  }
}
