package com.ben.company.service.push;

import com.ben.bot.vo.PushNotificationVO;
import com.ben.bot.vo.PushScheduleVO;
import com.ben.common.domain.PageFilter;
import com.ben.company.vo.push.PushResponse;

/**
 * @author lomofu
 * @date 2020/3/22 15:00
 */
public interface PushService {
  PushResponse getMessageById(String id);

  PushResponse getMessagesByAccountId(String accountId, PageFilter<String> pageFilter);

  PushResponse pushNotification(PushNotificationVO pushNotificationVO);

  PushResponse pushSchedule(String token,PushScheduleVO pushScheduleVO);
}
