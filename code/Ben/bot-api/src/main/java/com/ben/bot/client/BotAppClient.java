package com.ben.bot.client;

import com.ben.bot.dto.app.NotificationForSSEDto;
import com.ben.bot.vo.BotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ben.bot.constant.CommonConstant.SERVICE_APP_NAME;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;

/**
 * @author lomofu
 * @date 2020/3/23 16:20
 */
@FeignClient(
    name = SERVICE_APP_NAME,
    path = "/api/bot/app",
    url = "${app.endpoint.bot-service-endpoint}")
public interface BotAppClient {
  @PostMapping(value = "/notification")
  BotResponse sendMessageWithSSE(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody NotificationForSSEDto notificationForSSEDto);
}
