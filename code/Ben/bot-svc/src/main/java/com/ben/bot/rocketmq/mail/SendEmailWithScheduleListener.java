package com.ben.bot.rocketmq.mail;

import com.alibaba.fastjson.JSON;
import com.ben.bot.config.EndpointConfiguration;
import com.ben.bot.dto.mail.EmailScheduleDto;
import com.ben.bot.dto.mail.JobDto;
import com.ben.bot.dto.mail.ScheduleDto;
import com.ben.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.ben.bot.rocketmq.mail.SendEmailWithScheduleListener.BOT_MAIL_SCHEDULE_CONSUMER;
import static com.ben.bot.service.impl.BotMailServiceImpl.SEND_EMAIL_WITH_SCHEDULE;

/**
 * @author lomofu
 * @date 2020/3/25 01:41
 */
@Slf4j
@Component
@RocketMQMessageListener(
    topic = SEND_EMAIL_WITH_SCHEDULE,
    consumerGroup = BOT_MAIL_SCHEDULE_CONSUMER)
public class SendEmailWithScheduleListener implements RocketMQListener<ScheduleDto> {
  public static final String API_MAIL_SCHEDULE = "/api/mail/pushSchedule";

  public static final String BOT_MAIL_SCHEDULE_CONSUMER = "bot-mail-schedule-consumer";

  @Resource private EndpointConfiguration endpointConfiguration;

  @Override
  public void onMessage(ScheduleDto scheduleDto) {
    List<JobDto> jobDtoList =
        scheduleDto.getList().stream()
            .map(e -> JSON.parseObject(e, JobDto.class))
            .collect(Collectors.toList());
    EmailScheduleDto emailScheduleDto =
        EmailScheduleDto.builder().to(scheduleDto.getTo()).list(jobDtoList).build();
    log.info(emailScheduleDto.toString());
    WebClient webClient = WebClient.create(endpointConfiguration.getMailServiceEndpoint());
    BaseResponse response =
        webClient
            .post()
            .uri(API_MAIL_SCHEDULE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(emailScheduleDto), EmailScheduleDto.class)
            .retrieve()
            .bodyToMono(BaseResponse.class)
            .block();
    assert response != null;
    log.info("-> SendEmailWithSchedule:响应码 {}", response.getCode());
  }
}
