package com.ben.bot.controller;

import com.ben.bot.dto.app.NotificationForSSEDto;
import com.ben.bot.service.BotAppService;
import com.ben.bot.vo.BotResponse;
import com.ben.common.annotation.Authorize;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;

/**
 * @author lomofu
 * @date 2020/3/23 16:23
 */
@RestController
@RequestMapping("/api/bot/app")
public class BotAppController {
  @Resource private BotAppService botAppService;

  @PostMapping(value = "/notification")
  public Mono<BotResponse> sendMessageWithSSE(
      @RequestBody NotificationForSSEDto notificationForSSEDto) throws JsonProcessingException {
    return Mono.just(botAppService.sendSSEWithNotification(notificationForSSEDto));
  }
}
