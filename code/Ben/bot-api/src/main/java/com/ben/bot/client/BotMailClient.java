package com.ben.bot.client;

import com.ben.bot.dto.mail.*;
import com.ben.bot.vo.BotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ben.bot.constant.CommonConstant.SERVICE_MAIL_NAME;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE;

/**
 * @author lomofu
 * @date 2020/3/4 04:59
 */
@FeignClient(
    name = SERVICE_MAIL_NAME,
    path = "/api/bot/mail",
    url = "${app.endpoint.bot-service-endpoint}")
public interface BotMailClient {
  /**
   * feign: 发送创建新管理员账户邮件
   *
   * @param service 服务调用端描述
   * @param createNewAdminDto 创建新账户视图类
   * @return BotResponse
   */
  @PostMapping(path = "/createNewAdmin")
  BotResponse sendEmailWithCreateNewAdmin(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody CreateNewAdminDto createNewAdminDto);

  /**
   * feign: 发送创建新管理员账户成功邮件
   *
   * @param service 服务调用端描述
   * @param createNewEmployeeSuccessDto 创建新账户成功视图类
   * @return BotResponse
   */
  @PostMapping(path = "/createNewAdminSuccess")
  BotResponse sendEmailWithCreateNewAdminSuccess(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto);

  /**
   * feign: 发送创建新员工账户邮件
   *
   * @param service 服务调用端描述
   * @param createNewEmployeeDto 创建新员工账户视图类
   * @return BotResponse
   */
  @PostMapping(path = "/createNewEmployee")
  BotResponse sendEmailWithCreateNewEmployee(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody CreateNewEmployeeDto createNewEmployeeDto);

  /**
   * feign: 发送创建新员工成功邮件
   *
   * @param service 服务调用端描述
   * @param createNewEmployeeSuccessDto 创建新员工成功视图类
   * @return BotResponse
   */
  @PostMapping(path = "/createNewEmployeeSuccess")
  BotResponse sendEmailWithCreateNewEmployeeSuccess(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody CreateNewEmployeeSuccessDto createNewEmployeeSuccessDto);

  /**
   * feign: 发送登陆错误次数超过上限警告邮件
   *
   * @param service 服务调用端描述
   * @param loginToMuchDto 登陆过多视图类
   * @return BotResponse
   */
  @PostMapping(path = "/loginToMuch")
  BotResponse sendEmailWithLoginToMuch(
      @RequestHeader(AUTHORIZATION_SERVICE) String service,
      @RequestBody LoginToMuchDto loginToMuchDto);

  /**
   * feign: 公告推送邮件
   *
   * @param notificationDto 推送视图类
   * @return BotResponse
   */
  @PostMapping(path = "/notification")
  BotResponse sendEmailWithNotification(@RequestBody NotificationDto notificationDto);

  /**
   * feign: 排班推送邮件
   *
   * @param service 服务调用端描述
   * @param scheduleDto 推送视图类
   * @return BotResponse
   */
  @PostMapping(path = "/schedule")
  BotResponse sendEmailWithSchedule(
      @RequestHeader(AUTHORIZATION_SERVICE) String service, @RequestBody ScheduleDto scheduleDto);
}
