package com.ben.company.service.push.notification.impl;

import com.ben.account.dto.AccountDto;
import com.ben.bot.client.BotAppClient;
import com.ben.bot.dto.app.NotificationForSSEDto;
import com.ben.bot.vo.BotResponse;
import com.ben.company.domain.Job;
import com.ben.company.domain.Message;
import com.ben.company.service.push.notification.SendContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;

/**
 * @author lomofu
 * @date 2020/3/23 16:13
 */
@Slf4j
@Service
public class SendContextWithSSE implements SendContext {
  @Resource private BotAppClient botAppClient;

  @Override
  public void pushNotification(Message message, List<AccountDto> accountDtoList) {}

  @Override
  public void pushNotification(Message message, String companyId) {
    BotResponse response =
        botAppClient.sendMessageWithSSE(
            AUTHORIZATION_SERVICE_COMPANY,
            NotificationForSSEDto.builder()
                .companyId(companyId)
                .title(message.getTitle())
                .content(message.getContent())
                .build());
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
  public void pushSchedule(List<Job> jobList, List<AccountDto> accountDtoList) {}
}
