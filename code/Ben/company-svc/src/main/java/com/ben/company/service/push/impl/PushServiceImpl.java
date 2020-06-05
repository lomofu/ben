package com.ben.company.service.push.impl;

import com.ben.account.client.AccountClient;
import com.ben.account.dto.AccountDto;
import com.ben.bot.vo.PushNotificationVO;
import com.ben.bot.vo.PushScheduleVO;
import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.RedisHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.CompanyMappingAccountMapper;
import com.ben.company.dao.MessageMapper;
import com.ben.company.dao.MessageMappingAccountMapper;
import com.ben.company.domain.CompanyMappingAccount;
import com.ben.company.domain.Job;
import com.ben.company.domain.Message;
import com.ben.company.domain.MessageMappingAccount;
import com.ben.company.dto.push.MessageDto;
import com.ben.company.exception.PushException;
import com.ben.company.service.job.JobService;
import com.ben.company.service.push.PushService;
import com.ben.company.service.push.notification.impl.SendContextWithEmail;
import com.ben.company.service.push.notification.impl.SendContextWithSMS;
import com.ben.company.service.push.notification.impl.SendContextWithSSE;
import com.ben.company.vo.push.PushResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.common.enums.ResultCode.PARAM_MISS;
import static com.ben.company.enums.PushEnum.*;

/**
 * @author lomofu
 * @date 2020/3/22 15:00
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
  public static final String LOCK_PUSH_NOTIFICATION = "LockPushNotification";

  public static final String MAIL = "mail";

  public static final String APP = "app";

  public static final String PHONE = "phone";

  @Resource private MessageMapper messageMapper;

  @Resource private CompanyMappingAccountMapper companyMappingAccountMapper;

  @Resource private MessageMappingAccountMapper messageMappingAccountMapper;

  @Resource private AccountClient accountClient;

  @Resource private JobService jobService;

  @Resource private SendContextWithEmail sendContextWithEmail;

  @Resource private SendContextWithSSE sendContextWithSSE;

  @Resource private SendContextWithSMS sendContextWithSMS;

  @Resource private RedisHelper redisHelper;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  public PushResponse getMessageById(String id) {
    return Optional.ofNullable(messageMapper.selectOne(Message.builder().id(id).build()))
        .map(
            e ->
                MessageDto.builder()
                    .id(e.getId())
                    .title(e.getTitle())
                    .content(e.getContent())
                    .createTime(e.getCreateTime())
                    .accountName(
                        Optional.ofNullable(accountClient.getAccountByIdForFeign(e.getCreateBy()))
                            .map(AccountDto::getName)
                            .orElse(null))
                    .avatarUrl(
                        Optional.ofNullable(accountClient.getAccountByIdForFeign(e.getCreateBy()))
                            .map(AccountDto::getAvatarUrl)
                            .orElse(null))
                    .build())
        .map(PushResponse::new)
        .orElseThrow(() -> new PushException(MESSAGE_NOT_FOUND));
  }

  @Override
  public PushResponse getMessagesByAccountId(String accountId, PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();
    PageInfo<Message> pageInfo =
        PageHelper.startPage(filter.getPageNumber(), filter.getPageSize())
            .doSelectPageInfo(() -> messageMapper.selectMessagesByAccountId(accountId));
    return new PushResponse(this.getPageVO(pageInfo));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public PushResponse pushNotification(PushNotificationVO pushNotificationVO) {
    Message message = getMessage(pushNotificationVO);
    List<String> selected = pushNotificationVO.getSelected();
    List<AccountDto> accounts = this.getCompanyAccounts(pushNotificationVO.getCompanyId());
    String key = LOCK_PUSH_NOTIFICATION.concat(pushNotificationVO.getCreateBy());
    try {
      locker.lock(key, 10L);
      messageMapper.insert(message);
      accounts.forEach(
          e ->
              messageMappingAccountMapper.insert(
                  MessageMappingAccount.builder()
                      .messageId(message.getId())
                      .accountId(e.getId())
                      .build()));

      if (selected.contains(APP)) {
        CompletableFuture.runAsync(
            () -> sendContextWithSSE.pushNotification(message, pushNotificationVO.getCompanyId()),
            appConfiguration.getAsyncExecutor());
      }

      if (selected.contains(MAIL)) {
        CompletableFuture.runAsync(
                () -> sendContextWithEmail.pushNotification(message, accounts),
                appConfiguration.getAsyncExecutor())
            .exceptionally(
                e -> {
                  log.error(
                      METHOD_ERROR_TEMPLATE,
                      Thread.currentThread().getName(),
                      this.getClass().getName(),
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      Thread.currentThread().getStackTrace()[1].getLineNumber(),
                      e.getMessage());
                  return null;
                });
      }

      if (selected.contains(PHONE)) {
        CompletableFuture.runAsync(
            () -> sendContextWithSMS.pushNotification(message, accounts),
            appConfiguration.getAsyncExecutor());
      }

      return new PushResponse(PUSH_NOTIFICATION_SUCCESS);
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new PushException(PUSH_NOTIFICATION_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public PushResponse pushSchedule(String token, PushScheduleVO pushScheduleVO) {
    PushScheduleVO vo =
        Optional.ofNullable(pushScheduleVO).orElseThrow(() -> new CustomException(PARAM_MISS));
    List<Job> jobs = jobService.getJobRangeByProjectId(pushScheduleVO.getProjectId());
    log.info("jobs: {} projectId: {}", jobs, vo.getProjectId());
    CompletableFuture<List<AccountDto>> listCompletableFuture =
        CompletableFuture.supplyAsync(
            () -> this.getProjectAccounts(jobs), appConfiguration.getAsyncExecutor());

    if (CollectionUtils.isEmpty(jobs)) {
      throw new PushException(NO_SCHEDULE);
    }

    try {
      jobService.updateJobPush(jobs);
      CompletableFuture.runAsync(
          () ->
              redisHelper.del(
                  "job-svc::"
                      .concat(token)
                      .concat(":findJobListByProjectId:")
                      .concat(vo.getProjectId())),
          appConfiguration.getAsyncExecutor());
      if (vo.getSelected().contains(MAIL)) {
        sendContextWithEmail.pushSchedule(jobs, listCompletableFuture.join());
      }

      if (vo.getSelected().contains(PHONE)) {
        sendContextWithSMS.pushSchedule(jobs, listCompletableFuture.join());
      }
      return new PushResponse(PUSH_SCHEDULE_SUCCESS);
    } catch (Exception e) {
      log.error(
          METHOD_ERROR_TEMPLATE,
          Thread.currentThread().getName(),
          this.getClass().getName(),
          Thread.currentThread().getStackTrace()[1].getMethodName(),
          Thread.currentThread().getStackTrace()[1].getLineNumber(),
          e.getMessage());
      throw new PushException(PUSH_SCHEDULE_FAIL);
    }
  }

  protected Message getMessage(PushNotificationVO pushNotificationVO) {
    return Optional.ofNullable(pushNotificationVO)
        .map(
            e ->
                Message.builder()
                    .title(e.getTitle())
                    .content(e.getContent())
                    .createBy(e.getCreateBy())
                    .createTime(new Date())
                    .build())
        .orElseThrow(() -> new CustomException(PARAM_MISS));
  }

  protected List<AccountDto> getCompanyAccounts(String companyId) {
    String[] array =
        companyMappingAccountMapper
            .select(CompanyMappingAccount.builder().companyId(companyId).build()).stream()
            .map(CompanyMappingAccount::getAccountId)
            .distinct()
            .toArray(String[]::new);
    return accountClient.getAccountByListIdForFeign(AUTHORIZATION_SERVICE_COMPANY, array);
  }

  protected List<AccountDto> getProjectAccounts(List<Job> list) {
    String[] array = list.stream().map(Job::getAccountId).distinct().toArray(String[]::new);
    return accountClient.getAccountByListIdForFeign(AUTHORIZATION_SERVICE_COMPANY, array);
  }

  protected PageVO<MessageDto> getPageVO(PageInfo<Message> pageInfo) {
    return PageVO.<MessageDto>builder()
        .pages(pageInfo.getPages())
        .total(pageInfo.getTotal())
        .list(this.converseToMessageDto(pageInfo.getList()))
        .hasNextPage(pageInfo.isHasNextPage())
        .build();
  }

  protected List<MessageDto> converseToMessageDto(List<Message> list) {
    return list.stream()
        .map(
            e ->
                MessageDto.builder()
                    .id(e.getId())
                    .title(e.getTitle())
                    .content(e.getContent())
                    .createTime(e.getCreateTime())
                    .accountName(
                        Optional.ofNullable(accountClient.getAccountByIdForFeign(e.getCreateBy()))
                            .map(AccountDto::getName)
                            .orElse(null))
                    .avatarUrl(
                        Optional.ofNullable(accountClient.getAccountByIdForFeign(e.getCreateBy()))
                            .map(AccountDto::getAvatarUrl)
                            .orElse(null))
                    .build())
        .collect(Collectors.toList());
  }
}
