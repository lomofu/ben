package com.ben.bot.service;

import com.ben.bot.dto.app.NotificationForSSEDto;
import com.ben.bot.vo.BotResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author lomofu
 * @date 2020/3/23 16:24
 */
public interface BotAppService {
  BotResponse sendSSEWithNotification(NotificationForSSEDto notificationForSSEDto)
      throws JsonProcessingException;
}
