package com.ben.company.service.job.impl;

import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.common.utils.TimeHelper;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.JobMapper;
import com.ben.company.dao.JobMappingAccountMapper;
import com.ben.company.domain.Job;
import com.ben.company.domain.JobMappingAccount;
import com.ben.company.dto.job.JobCountDto;
import com.ben.company.exception.JobException;
import com.ben.company.service.job.JobBaseService;
import com.ben.company.service.job.JobService;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.job.JobVO;
import com.ben.company.vo.job.NewJobVO;
import com.ben.company.vo.job.UpdateJobVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ben.common.enums.ResultCode.PARAM_MISS;
import static com.ben.common.utils.TimeHelper.*;
import static com.ben.company.constant.CommonConstant.SELECT_ACTIVE;
import static com.ben.company.constant.CommonConstant.SELECT_ID;
import static com.ben.company.enums.JobEnum.*;

/**
 * @author lomofu
 * @date 2020/2/14 15:12
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "job-svc")
public class JobServiceImpl implements JobService {
  public static final String UPDATE_SINGLE_JOB_PUSH = "updateSingleJobPush";

  public static final String CREATE_NEW_JOB = "createNewJob";

  public static final String UPDATE_JOB = "updateJob";

  @Resource private JobMapper jobMapper;

  @Resource private JobMappingAccountMapper jobMappingAccountMapper;

  @Resource private JobBaseService jobBaseService;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  public List<Job> getJobRangeByProjectId(String projectId) {
    return jobMapper.selectJobsRangeProject(
        TimeHelper.converseDateToString(TimeHelper.nowWeekStart()),
        TimeHelper.converseDateToString(TimeHelper.nowWeekEnd()),
        projectId);
  }

  @Override
  public void updateJobPush(List<Job> list) {
    list.forEach(this::updateSingleJobPush);
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateSingleJobPush(Job job) {
    String key = UPDATE_SINGLE_JOB_PUSH.concat(job.getId());
    try {
      locker.lock(key);
      jobMapper.updateByPrimaryKeySelective(job.toBuilder().publish(true).build());
    } catch (Exception e) {
      throw new JobException(JOB_UPDATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public JobResponse getJobListByProjectId(String token, String id) {
    return Optional.ofNullable(jobBaseService.findJobListByProjectId(token, id))
        .map(e -> e.stream().map(this::converseJobToJobVO).collect(Collectors.toList()))
        .map(JobResponse::new)
        .orElseThrow(() -> new JobException(WITHOUT_RES));
  }

  @Override
  public JobResponse getThisWeekJobListByAccountId(PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();
    return Optional.ofNullable(jobBaseService.findJobListByAccountIdThisWeek(filter))
        .map(
            e ->
                PageVO.<JobVO>builder()
                    .pages(e.getPages())
                    .total(e.getTotal())
                    .list(this.converseJobToJobVO(e.getList()))
                    .build())
        .map(JobResponse::new)
        .orElseGet(JobResponse::new);
  }

  @Override
  public JobResponse getJobListByAccountId(PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();
    return Optional.ofNullable(jobBaseService.findJobListByAccountId(filter))
        .map(
            e ->
                PageVO.<JobVO>builder()
                    .pages(e.getPages())
                    .total(e.getTotal())
                    .list(this.converseJobToJobVO(e.getList()))
                    .build())
        .map(JobResponse::new)
        .orElseGet(JobResponse::new);
  }

  @Override
  public JobResponse getJobById(String token, String id) {
    return Optional.ofNullable(jobBaseService.findJobByJobId(token, id))
        .map(this::converseJobToJobVO)
        .map(JobResponse::new)
        .orElseThrow(() -> new JobException(JOB_IS_EMPTY));
  }

  @Override
  public JobResponse getTotalJobCountByProjectId(String projectId) {
    CompletableFuture<Integer> unPublishCount =
        CompletableFuture.supplyAsync(
                () ->
                    jobMapper.selectCount(
                        Job.builder().publish(false).active(true).projectId(projectId).build()))
            .thenApply(integer -> Optional.ofNullable(integer).orElse(0));
    CompletableFuture<Integer> publishCount =
        CompletableFuture.supplyAsync(
                () ->
                    jobMapper.selectCount(
                        Job.builder().publish(true).active(true).projectId(projectId).build()))
            .thenApply(integer -> Optional.ofNullable(integer).orElse(0));
    return new JobResponse(
        JobCountDto.builder()
            .pubCount(publishCount.join())
            .unPubCount(unPublishCount.join())
            .total(publishCount.join() + unPublishCount.join())
            .build());
  }

  @Override
  @CacheEvict(key = "#token.concat(':findJobListByProjectId:').concat(#newJobVO.getProjectId())")
  @Transactional(rollbackFor = Exception.class)
  public JobResponse createNewJob(String token, NewJobVO newJobVO) {
    Job job =
        Optional.ofNullable(newJobVO)
            .map(
                e ->
                    Job.builder()
                        .name(e.getName())
                        .description(e.getDescription())
                        .color(e.getColor())
                        .start(converseStringToDate(e.getStart()))
                        .end(converseStringToDate(e.getEnd()))
                        .projectId(e.getProjectId())
                        .accountId(e.getAccountId())
                        .full(e.isFull())
                        .active(true)
                        .publish(false)
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));

    // 当事件不是全天时,进行时间段事件校验
    if (!job.getFull() && isHaveJob(job.getAccountId(), job.getStart(), job.getEnd())) {
      throw new JobException(JOB_IS_EXIST);
    }

    String key = CREATE_NEW_JOB.concat(job.getProjectId());
    try {
      locker.lock(key, 20L);
      int insert = jobMapper.insert(job);
      int insert1 =
          jobMappingAccountMapper.insert(
              JobMappingAccount.builder().accountId(job.getAccountId()).jobId(job.getId()).build());
      if (insert > 0 && insert1 > 0) {
        return new JobResponse(JOB_CREATED_SUCCESS, job.getId());
      }
      throw new JobException(JOB_CREATED_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_CREATED_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findJobByJobId:').concat(#updateJobVO.getId())"),
        @CacheEvict(
            key = "#token.concat(':findJobListByProjectId:').concat(#updateJobVO.getProjectId())")
      })
  public JobResponse updateJob(String token, UpdateJobVO updateJobVO) {
    Job job =
        Optional.ofNullable(updateJobVO)
            .map(
                e ->
                    Job.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .color(e.getColor())
                        .description(e.getDescription())
                        .start(converseStringToDate(e.getStart()))
                        .end(converseStringToDate(e.getEnd()))
                        .full(e.isFull())
                        .accountId(e.getAccountId())
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));

    if (this.isModified(job)) {
      if (!job.getFull() && isHaveJob(job.getAccountId(), job.getStart(), job.getEnd())) {
        throw new JobException(JOB_IS_EXIST);
      }
      throw new JobException(JOB_IS_NOT_MODIFY);
    }

    String key = UPDATE_JOB.concat(job.getId());
    try {
      locker.lock(key, 10L);
      int update = jobMapper.updateByPrimaryKeySelective(job);
      if (update > 0) {
        return new JobResponse(JOB_UPDATE_SUCCESS, job.getId());
      }
      throw new JobException(JOB_UPDATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JobException(JOB_UPDATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public JobResponse deleteJobById(String token, String jobId) {
    try {
      jobBaseService.deleteJobByJobId(token, jobId);
      return new JobResponse(JOB_DELETE_SUCCESS);
    } catch (Exception e) {
      throw new JobException(JOB_DELETE_FAIL);
    }
  }

  protected boolean isHaveJob(String accountId, Date start, Date end) {
    String startTime = converseDateToString(start);
    String endTime = converseDateToString(end);

    CompletableFuture<List<Job>> result =
        CompletableFuture.supplyAsync(
            () ->
                jobMapper.selectExistJobBetweenBeginAndEndForAccount(startTime, endTime, accountId),
            appConfiguration.getAsyncExecutor());

    CompletableFuture<List<Job>> result1 =
        CompletableFuture.supplyAsync(
            () ->
                jobMapper.selectExistJobBetweenBeginAndEndForAccount1(
                    startTime, endTime, accountId),
            appConfiguration.getAsyncExecutor());

    CompletableFuture<List<Job>> result2 =
        CompletableFuture.supplyAsync(
            () ->
                jobMapper.selectExistJobBetweenBeginAndEndForAccount2(
                    startTime, endTime, accountId),
            appConfiguration.getAsyncExecutor());

    CompletableFuture<List<Job>> result3 =
        CompletableFuture.supplyAsync(
            () ->
                jobMapper.selectExistJobBetweenBeginAndEndForAccount3(
                    startTime, endTime, accountId),
            appConfiguration.getAsyncExecutor());

    CompletableFuture<Boolean> future =
        result
            .thenCombine(
                result1, (a, b) -> CollectionUtils.isEmpty(a) && CollectionUtils.isEmpty(b))
            .thenCombine(result2, (c, d) -> c && CollectionUtils.isEmpty(d))
            .thenCombine(result3, (e, f) -> e && CollectionUtils.isEmpty(f));

    return Optional.ofNullable(future.join()).map(Boolean.FALSE::equals).orElse(true);
  }

  protected boolean isModified(Job job) {
    return Optional.ofNullable(
            jobMapper.selectOneByExample(
                Example.builder(Job.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_ID, job.getId())
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .map(
            e -> {
              boolean index =
                  e.getName().equals(job.getName())
                      && e.getColor().equals(job.getColor())
                      && e.getFull().equals(job.getFull())
                      && TimeHelper.compareTwoDateIsEquals(e.getStart(), job.getStart())
                      && TimeHelper.compareTwoDateIsEquals(e.getEnd(), job.getEnd());
              if (StringUtils.hasText(e.getDescription())) {
                return index && e.getDescription().equals(job.getDescription());
              }
              return index;
            })
        .orElse(true);
  }

  protected JobVO converseJobToJobVO(Job job) {
    JobVO jobVO = new JobVO();
    BeanUtils.copyProperties(job, jobVO);
    return jobVO
        .toBuilder()
        .start(converseDateToString(job.getStart()))
        .end(converseDateToString(job.getEnd()))
        .build();
  }

  protected List<JobVO> converseJobToJobVO(List<Object> jobList) {
    return jobList.stream()
        .map(
            e -> {
              Job j = (Job) e;
              JobVO jobVO = JobVO.builder().build();
              BeanUtils.copyProperties(j, jobVO);
              if (jobVO.isFull()) {
                jobVO.setStart(converseSimpleDateToString(j.getStart()));
                jobVO.setEnd(converseSimpleDateToString(j.getEnd()));
              } else {
                jobVO.setStart(converseDateToString(j.getStart()));
                jobVO.setEnd(converseDateToString(j.getEnd()));
              }
              return jobVO;
            })
        .collect(Collectors.toList());
  }
}
