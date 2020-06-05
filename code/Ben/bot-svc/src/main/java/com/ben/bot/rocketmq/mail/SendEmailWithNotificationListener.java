package com.ben.bot.rocketmq.mail;

import com.ben.bot.config.EndpointConfiguration;
import com.ben.bot.dto.mail.NotificationDto;
import com.ben.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.ben.bot.rocketmq.mail.SendEmailWithNotificationListener.BOT_MAIL_NOTIFICATION_CONSUMER;
import static com.ben.bot.service.impl.BotMailServiceImpl.SEND_EMAIL_WITH_NOTIFICATION;

/**
 * @author lomofu
 * @date 2020/3/22 19:44
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = SEND_EMAIL_WITH_NOTIFICATION,
    consumerGroup = BOT_MAIL_NOTIFICATION_CONSUMER)
public class SendEmailWithNotificationListener implements RocketMQListener<NotificationDto> {
  public static final String BOT_MAIL_NOTIFICATION_CONSUMER = "bot-mail-notification-consumer";
  public static final String API_MAIL_NOTIFICATION = "/api/mail/pushNotification";

  @Resource private EndpointConfiguration endpointConfiguration;

  @Override
  public void onMessage(NotificationDto notificationDto) {
    log.info(notificationDto.toString());
    WebClient webClient = WebClient.create(endpointConfiguration.getMailServiceEndpoint());
    BaseResponse response =
        webClient
            .post()
            .uri(API_MAIL_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(notificationDto), NotificationDto.class)
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block();
    assert response != null;
    log.info("-> SendEmailWithNotification:响应码 {}", response.getCode());
  }
}
