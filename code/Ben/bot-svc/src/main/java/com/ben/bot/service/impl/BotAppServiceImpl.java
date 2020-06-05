package com.ben.bot.service.impl;

import com.ben.bot.dto.app.NotificationForSSEDto;
import com.ben.bot.service.BotAppService;
import com.ben.bot.vo.BotResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lomofu
 * @date 2020/3/23 16:24
 */
@Slf4j
@Service
public class BotAppServiceImpl implements BotAppService {
  @Resource private GoEasy goEasy;

  @Resource private ObjectMapper objectMapper;

  @Override
  public BotResponse sendSSEWithNotification(NotificationForSSEDto notificationForSSEDto)
      throws JsonProcessingException {
    log.info(notificationForSSEDto.toString());
    goEasy.publish(
        notificationForSSEDto.getCompanyId(),
        objectMapper.writeValueAsString(notificationForSSEDto),
        new PublishListener() {
          @Override
          public void onSuccess() {
            log.info("公告发送成功: {}", notificationForSSEDto.toString());
          }

          @Override
          public void onFailed(GoEasyError error) {
            log.error("消息发布失败, 错误码: {},\n错误信息: {} ", error.getCode(), error.getContent());
          }
        });
    return new BotResponse();
  }
}
