package com.ben.bot.rocketmq.mail;

import com.ben.bot.config.EndpointConfiguration;
import com.ben.bot.dto.mail.CreateNewEmployeeSuccessDto;
import com.ben.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

import static com.ben.bot.rocketmq.mail.SendEmailWithCreateNewEmployeeSuccessListener.BOT_MAIL_CONSUMER;
import static com.ben.bot.service.impl.BotMailServiceImpl.SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE_SUCCESS;

/**
 * @author lomofu
 * @date 2020/3/5 18:12
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE_SUCCESS,
    consumerGroup = BOT_MAIL_CONSUMER)
public class SendEmailWithCreateNewEmployeeSuccessListener
    implements RocketMQListener<CreateNewEmployeeSuccessDto> {

  public static final String BOT_MAIL_CONSUMER = "bot-mail-create-new-employee-success-consumer";

  public static final String API_MAIL_CREATE_NEW_EMPLOYEE_SUCCESS =
      "/api/mail/createNewEmployeeSuccess";

  @Resource private EndpointConfiguration endpointConfiguration;

  @Override
  public void onMessage(CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto) {
    WebClient webClient = WebClient.create(endpointConfiguration.getMailServiceEndpoint());
    BaseResponse response =
        webClient
            .post()
            .uri(API_MAIL_CREATE_NEW_EMPLOYEE_SUCCESS)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(createNewEmployeeSuccessDto), CreateNewEmployeeSuccessDto.class)
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block();
    assert response != null;
    log.info("响应码 {}", response.getCode());
  }
}
