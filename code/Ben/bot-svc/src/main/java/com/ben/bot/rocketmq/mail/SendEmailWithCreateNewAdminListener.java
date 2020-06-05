package com.ben.bot.rocketmq.mail;

import com.ben.bot.config.EndpointConfiguration;
import com.ben.bot.dto.mail.CreateNewAdminDto;
import com.ben.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.ben.bot.rocketmq.mail.SendEmailWithCreateNewAdminListener.BOT_MAIL_CONSUMER;
import static com.ben.bot.service.impl.BotMailServiceImpl.SEND_EMAIL_WITH_CREATE_NEW_ADMIN;

/**
 * @author lomofu
 * @date 2020/3/4 22:09
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = SEND_EMAIL_WITH_CREATE_NEW_ADMIN,
    consumerGroup = BOT_MAIL_CONSUMER)
public class SendEmailWithCreateNewAdminListener implements RocketMQListener<CreateNewAdminDto> {

  public static final String BOT_MAIL_CONSUMER = "bot-mail-create-new-admin-consumer";

  public static final String API_MAIL_CREATE_NEW_ADMIN = "/api/mail/createNewAdmin";

  @Resource private EndpointConfiguration endpointConfiguration;

  @Override
  public void onMessage(CreateNewAdminDto createNewAdminDto) {
    WebClient webClient = WebClient.create(endpointConfiguration.getMailServiceEndpoint());
    BaseResponse response =
        webClient
            .post()
            .uri(API_MAIL_CREATE_NEW_ADMIN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(createNewAdminDto), CreateNewAdminDto.class)
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block();
    assert response != null;
    log.info("响应码 {}", response.getCode());
  }
}
