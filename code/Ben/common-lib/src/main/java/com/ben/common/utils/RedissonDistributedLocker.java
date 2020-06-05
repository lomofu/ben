package com.ben.common.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * @author lomofu
 * @date 2020/3/20 15:22
 */
@Component
public class RedissonDistributedLocker {
  @Resource private RedissonClient redissonClient;

  /**
   * 加锁
   *
   * @param lockKey
   * @return RLock
   */
  public RLock lock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock();
    return lock;
  }
  /**
   * 加锁，过期自动释放
   *
   * @param lockKey
   * @param leaseTime 自动释放锁时间 秒
   * @return
   */
  public RLock lock(String lockKey, long leaseTime) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(leaseTime, TimeUnit.SECONDS);
    return lock;
  }

  /**
   * 加锁，过期自动释放，时间单位传入
   *
   * @param lockKey
   * @param unit 时间单位
   * @param leaseTime 上锁后自动释放时间
   * @return
   */
  public RLock lock(String lockKey, TimeUnit unit, long leaseTime) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(leaseTime, unit);
    return lock;
  }

  /**
   * 释放锁
   *
   * @param lockKey
   */
  public void unlock(@NotBlank String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.unlock();
  }

  /**
   * 释放锁
   *
   * @param lock
   */
  public void unlock(RLock lock) {
    lock.unlock();
  }
}
