package com.ben.company.service.push.notification;

import com.ben.account.dto.AccountDto;
import com.ben.company.domain.Job;
import com.ben.company.domain.Message;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/23 02:10
 */
public interface SendContext {
  void pushNotification(Message message, List<AccountDto> accountDtoList);

  void pushNotification(Message message, String companyId);

  void pushSchedule(List<Job> jobList, List<AccountDto> accountDtoList);
}
