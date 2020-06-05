package com.ben.account.task;

import com.ben.account.service.PermissionService;
import com.ben.common.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lomofu
 * @date 2020/3/18 22:09
 */
@Slf4j
@Component
public class AccountSchedule {
  public static final String KEY = "account-svc::perm::findAll";

  public static final long TIME = 86400L;

  @Resource private RedisHelper redisHelper;

  @Resource private PermissionService permissionService;

  @Scheduled(cron = "0 0 4 * * ?")
  public void updateExpireDate() {
    log.info("更新权限列表缓存");
    redisHelper.set(KEY, permissionService.findAll(), TIME);
  }
}
