package com.ben.gateway.task;

import com.ben.gateway.dto.GatewayEntry;
import com.ben.common.utils.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;

/**
 * @author lomofu
 * @date 2020/3/4 01:30
 */
@Component
@Slf4j
public class GateWaySchedule {

  @Resource private RedisHelper redisHelper;

  @Resource private GatewayEntry gatewayEntry;

  @Scheduled(cron = "0 0/20 * * * ?")
  public void updateExpireDate() {
    log.info("更新缓存 {}", gatewayEntry.getUuid());
    redisHelper.set("gateway::" + gatewayEntry.getUuid(), gatewayEntry, DEFAULT_CACHE_TIME);
  }
}
