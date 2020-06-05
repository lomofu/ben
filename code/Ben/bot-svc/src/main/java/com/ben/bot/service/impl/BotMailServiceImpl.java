package com.ben.bot.service.impl;

import com.ben.bot.dto.mail.*;
import com.ben.bot.service.BotMailService;
import com.ben.bot.vo.BotResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.ben.bot.enums.BotEnum.SEND_NOTIFICATION_SUCCESS;
import static com.ben.bot.enums.BotEnum.SEND_SCHEDULE_SUCCESS;

/**
 * @author lomofu
 * @date 2020/3/4 05:35
 */
@Service
@Slf4j
public class BotMailServiceImpl implements BotMailService {
  public static final String SEND_EMAIL_WITH_CREATE_NEW_ADMIN = "sendEmailWithCreateNewAdmin";

  public static final String SEND_EMAIL_WITH_CREATE_NEW_ADMIN_SUCCESS =
      "sendEmailWithCreateNewAdminSuccess";

  public static final String SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE = "sendEmailWithCreateNewEmployee";

  public static final String SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE_SUCCESS =
      "sendEmailWithCreateNewEmployeeSuccess";

  public static final String SEND_EMAIL_WITH_LOGIN_TO_MUCH = "sendEmailWithLoginToMuch";

  public static final String SEND_EMAIL_WITH_NOTIFICATION = "sendEmailWithNotification";

  public static final String SEND_EMAIL_WITH_SCHEDULE = "sendEmailWithSchedule";

  @Resource private RocketMQTemplate rocketMQTemplate;

  @Override
  public BotResponse sendEmailWithCreateNewAdmin(CreateNewAdminDto createNewAdminDto) {
    rocketMQTemplate.convertAndSend(SEND_EMAIL_WITH_CREATE_NEW_ADMIN, createNewAdminDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendEmailWithCreateNewAdminSuccess(
      CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto) {
    rocketMQTemplate.convertAndSend(
        SEND_EMAIL_WITH_CREATE_NEW_ADMIN_SUCCESS, createNewEmployeeSuccessDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendEmailWithCreateNewEmployee(CreateNewEmployeeDto createNewEmployeeDto) {
    rocketMQTemplate.convertAndSend(SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE, createNewEmployeeDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendEmailWithCreateNewEmployeeSuccess(
      CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto) {
    rocketMQTemplate.convertAndSend(
        SEND_EMAIL_WITH_CREATE_NEW_EMPLOYEE_SUCCESS, createNewEmployeeSuccessDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendEmailWithLoginToMuch(LoginToMuchDto loginToMuchDto) {
    rocketMQTemplate.convertAndSend(SEND_EMAIL_WITH_LOGIN_TO_MUCH, loginToMuchDto);
    return new BotResponse();
  }

  @Override
  public BotResponse sendEmailWithNotification(NotificationDto notificationDto) {
    notificationDto
        .getList()
        .forEach(
            e ->
                rocketMQTemplate.convertAndSend(
                    SEND_EMAIL_WITH_NOTIFICATION,
                    NotificationDto.builder()
                        .to(e)
                        .title(notificationDto.getTitle())
                        .content(notificationDto.getContent())
                        .build()));
    return new BotResponse(SEND_NOTIFICATION_SUCCESS);
  }

  @Override
  public BotResponse sendEmailWithSchedule(ScheduleDto scheduleDto) {
    rocketMQTemplate.convertAndSend(SEND_EMAIL_WITH_SCHEDULE, scheduleDto);
    return new BotResponse(SEND_SCHEDULE_SUCCESS);
  }
}
