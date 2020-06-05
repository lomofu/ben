package com.ben.company.controller;

import com.ben.bot.vo.PushNotificationVO;
import com.ben.bot.vo.PushScheduleVO;
import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import com.ben.company.service.push.PushService;
import com.ben.company.vo.push.PushResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.constant.PermissionConstant.SYS_JOB_SEND;
import static com.ben.common.constant.PermissionConstant.SYS_MESSAGE_SEND;

/**
 * @author lomofu
 * @date 2020/3/22 21:14
 */
@Api(tags = "推送接口")
@RestController
@RequestMapping(path = "/api/push")
@Validated
public class PushController {

  @Resource private PushService pushService;

  @GetMapping(path = "/message")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取消息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "消息id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<PushResponse> getMessageById(@RequestParam @NotBlank String id) {
    return new WebAsyncTask<>(() -> pushService.getMessageById(id));
  }

  @GetMapping(path = "/message/{accountId}")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取账户的推送信息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "path",
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<PushResponse> getMessagesByAccountId(
      @PathVariable @NotBlank String accountId, PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> pushService.getMessagesByAccountId(accountId, pageFilter));
  }

  @PostMapping(path = "/notification")
  @CheckPerm(value = SYS_MESSAGE_SEND)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 推送公告")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "pushNotificationVO",
        value = "pushNotificationVO 视图类",
        dataType = "PushNotificationVO",
        required = true)
  })
  public WebAsyncTask<PushResponse> pushNotification(
      @RequestBody @Valid @NotNull PushNotificationVO pushNotificationVO) {
    return new WebAsyncTask<>(() -> pushService.pushNotification(pushNotificationVO));
  }

  @PostMapping(path = "/schedule")
  @CheckPerm(value = SYS_JOB_SEND)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 推送排班")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "pushScheduleVO",
        value = "pushScheduleVO 视图类",
        dataType = "PushScheduleVO",
        required = true)
  })
  public WebAsyncTask<PushResponse> pushSchedule(
          @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull PushScheduleVO pushScheduleVO) {
    return new WebAsyncTask<>(() -> pushService.pushSchedule(token,pushScheduleVO));
  }
}
