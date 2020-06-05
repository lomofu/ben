package com.ben.company.service.job.impl;

import com.ben.common.annotation.CacheExpire;
import com.ben.common.domain.PageFilter;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.common.utils.TimeHelper;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.JobMapper;
import com.ben.company.dao.JobMappingAccountMapper;
import com.ben.company.domain.Job;
import com.ben.company.domain.JobMappingAccount;
import com.ben.company.exception.JobException;
import com.ben.company.service.job.JobBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.company.constant.CommonConstant.SELECT_ACTIVE;
import static com.ben.company.constant.CommonConstant.SELECT_ID;
import static com.ben.company.constant.ProjectConstant.SELECT_PROJECT_ID;
import static com.ben.company.enums.JobEnum.JOB_DELETE_FAIL;
import static com.ben.company.enums.JobEnum.WITHOUT_RES;

/**
 * @author lomofu
 * @date 2020/2/29 01:13
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "job-svc")
public class JobBaseServiceImpl implements JobBaseService {
  public static final String DELETE_JOB_TABLE_BY_JOB_ID = "deleteJobTableByJobId";

  public static final String DELETE_JOB_MAPPING_ACCOUNT_BY_JOB_ID =
      "deleteJobMappingAccountByJobId";

  public static final String DELETE_JOB_MAPPING_ACCOUNT_BY_ACCOUNT_ID =
      "deleteJobMappingAccountByAccountId";

  public static final String DELETE_JOB_BY_PROJECT_ID = "deleteJobByProjectId";

  public static final String START_DESC = "start desc";

  @Resource private JobMapper jobMapper;

  @Resource private JobMappingAccountMapper jobMappingAccountMapper;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#projectId)",
      condition =
          "#token != null && #token.length()>0 && #projectId!=null && #projectId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public List<Job> findJobListByProjectId(String token, String projectId) {
    return Optional.ofNullable(
            jobMapper.selectByExample(
                Example.builder(Job.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_PROJECT_ID, projectId)
                            .andEqualTo(SELECT_ACTIVE, true))
                    .orderByAsc("start")
                    .build()))
        .orElseThrow(() -> new JobException(WITHOUT_RES));
  }

  @Override
  public PageInfo<Object> findJobListByAccountIdThisWeek(PageFilter<String> pageFilter) {
    return PageHelper.startPage(pageFilter.getPageNumber(), pageFilter.getPageSize(), START_DESC)
        .doSelectPageInfo(
            () ->
                jobMapper.selectJobsRangeAccount(
                    TimeHelper.converseDateToString(TimeHelper.nowWeekStart()),
                    TimeHelper.converseDateToString(TimeHelper.nowWeekEnd()),
                    pageFilter.getData()));
  }

  @Override
  public PageInfo<Object> findJobListByAccountId(PageFilter<String> pageFilter) {
    return PageHelper.startPage(pageFilter.getPageNumber(), pageFilter.getPageSize(), START_DESC)
        .doSelectPageInfo(
            () ->
                jobMapper.select(
                    Job.builder().accountId(pageFilter.getData()).active(true).build()));
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#jobId)",
      condition = "#token != null && #token.length()>0 && #jobId!=null && #jobId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public Job findJobByJobId(String token, String jobId) {
    return jobMapper.selectOneByExample(
        Example.builder(Job.class)
            .where(Sqls.custom().andEqualTo(SELECT_ID, jobId).andEqualTo(SELECT_ACTIVE, true))
            .build());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteJobTableByJobId(String jobId) {
    String key = DELETE_JOB_TABLE_BY_JOB_ID.concat(jobId);
    try {
      locker.lock(key, 10L);
      jobMapper.updateByPrimaryKeySelective(Job.builder().id(jobId).active(false).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteJobMappingAccountByJobId(String id) {
    String key = DELETE_JOB_MAPPING_ACCOUNT_BY_JOB_ID.concat(id);
    try {
      locker.lock(key, 10L);
      jobMappingAccountMapper.delete(JobMappingAccount.builder().jobId(id).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int deleteJobMappingAccountByAccountId(String accountId) {
    String key = DELETE_JOB_MAPPING_ACCOUNT_BY_ACCOUNT_ID.concat(accountId);
    try {
      locker.lock(key, 10L);
      return jobMappingAccountMapper.delete(
          JobMappingAccount.builder().accountId(accountId).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findJobByJobId')", allEntries = true),
        @CacheEvict(key = "#token.concat(':findJobListByProjectId:').concat(#projectId)"),
      })
  public void deleteJobByProjectId(String token, String projectId) {
    String key = DELETE_JOB_BY_PROJECT_ID.concat(projectId);
    try {
      locker.lock(key, 30L);
      this.findJobListByProjectId(token, projectId)
          .forEach(e -> this.deleteJobByJobId(token, e.getId()));
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findJobByJobId:').concat(#id)"),
        @CacheEvict(key = "#token.concat(':findJobListByProjectId')", allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public void deleteJobByJobId(String token, String id) {
    try {
      CompletableFuture.runAsync(
              () -> this.deleteJobTableByJobId(id), appConfiguration.getAsyncExecutor())
          .exceptionally(
              e -> {
                log.error(
                    METHOD_ERROR_TEMPLATE,
                    Thread.currentThread().getName(),
                    this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    e.getMessage());
                throw new JobException(JOB_DELETE_FAIL);
              });
      CompletableFuture.runAsync(
              () -> this.deleteJobMappingAccountByJobId(id), appConfiguration.getAsyncExecutor())
          .exceptionally(
              e -> {
                log.error(
                    METHOD_ERROR_TEMPLATE,
                    Thread.currentThread().getName(),
                    this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    e.getMessage());
                throw new JobException(JOB_DELETE_FAIL);
              });
    } catch (Exception e) {
      throw new JobException(JOB_DELETE_FAIL);
    }
  }
}
