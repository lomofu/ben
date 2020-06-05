package com.ben.company.service.push.notification.impl;

import com.alibaba.fastjson.JSON;
import com.ben.account.dto.AccountDto;
import com.ben.bot.client.BotSmsClient;
import com.ben.bot.dto.mail.JobDto;
import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.bot.dto.sms.NotificationDto;
import com.ben.bot.vo.BotResponse;
import com.ben.common.utils.TimeHelper;
import com.ben.company.domain.Job;
import com.ben.company.domain.Message;
import com.ben.company.service.push.notification.SendContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;

/**
 * @author lomofu
 * @date 2020/3/24 21:21
 */
@Slf4j
@Service
public class SendContextWithSMS implements SendContext {
  @Resource private BotSmsClient botSmsClient;

  @Override
  public void pushNotification(Message message, List<AccountDto> accountDtoList) {
    List<String> collect =
        accountDtoList.stream().map(AccountDto::getPhoneNumber).collect(Collectors.toList());
    BotResponse response =
        botSmsClient.sendSMSWithNotification(
            AUTHORIZATION_SERVICE_COMPANY,
            NotificationDto.builder().title(message.getTitle()).list(collect).build());
    if (HttpStatus.OK.value() != response.getCode()) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          response.getMsg());
    }
  }

  @Override
  public void pushNotification(Message message, String companyId) {}

  @Override
  public void pushSchedule(List<Job> jobList, List<AccountDto> accountDtoList) {
    for (AccountDto dto : accountDtoList) {
      List<String> collect =
          jobList.stream()
              .filter(e -> e.getAccountId().equals(dto.getId()))
              .map(
                  e ->
                      JobDto.builder()
                          .name(e.getName())
                          .begin(TimeHelper.converseDateToString(e.getStart()))
                          .end(TimeHelper.converseDateToString(e.getEnd()))
                          .build())
              .map(JSON::toJSONString)
              .collect(Collectors.toList());

      BotResponse response =
          botSmsClient.sendSMSWithSchedule(
              AUTHORIZATION_SERVICE_COMPANY,
              ScheduleDto.builder().to(dto.getPhoneNumber()).list(collect).build());

      if (HttpStatus.OK.value() != response.getCode()) {
        log.error(
            METHOD_ERROR_TEMPLATE,
            Thread.currentThread().getName(),
            this.getClass().getName(),
            Thread.currentThread().getStackTrace()[1].getMethodName(),
            Thread.currentThread().getStackTrace()[1].getLineNumber(),
            response.getMsg());
      }
    }
  }
}
