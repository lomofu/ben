package com.ben.bot.rocketmq.mail;

import com.ben.bot.config.EndpointConfiguration;
import com.ben.bot.dto.mail.LoginToMuchDto;
import com.ben.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.ben.bot.rocketmq.mail.SendEmailWithLoginToMuchListener.BOT_MAIL_LOGIN_TO_MUCH_CONSUMER;
import static com.ben.bot.service.impl.BotMailServiceImpl.SEND_EMAIL_WITH_LOGIN_TO_MUCH;

/**
 * @author lomofu
 * @date 2020/3/5 21:50
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = SEND_EMAIL_WITH_LOGIN_TO_MUCH,
    consumerGroup = BOT_MAIL_LOGIN_TO_MUCH_CONSUMER)
public class SendEmailWithLoginToMuchListener implements RocketMQListener<LoginToMuchDto> {

  public static final String BOT_MAIL_LOGIN_TO_MUCH_CONSUMER = "bot-mail-login-toMuch-consumer";
  public static final String API_MAIL_LOGIN_TO_MUCH = "/api/mail/loginToMuch";

  @Resource private EndpointConfiguration endpointConfiguration;

  @Override
  public void onMessage(LoginToMuchDto loginToMuchDto) {
    WebClient webClient = WebClient.create(endpointConfiguration.getMailServiceEndpoint());
    BaseResponse response =
        webClient
            .post()
            .uri(API_MAIL_LOGIN_TO_MUCH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(loginToMuchDto), LoginToMuchDto.class)
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block();
    assert response != null;
    log.info("响应码 {}", response.getCode());
  }
}
