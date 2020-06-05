package com.ben.company.service.project.impl;

import com.ben.common.annotation.CacheExpire;
import com.ben.common.domain.PageFilter;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.ProjectMapper;
import com.ben.company.dao.ProjectMappingAccountMapper;
import com.ben.company.domain.Project;
import com.ben.company.domain.ProjectMappingAccount;
import com.ben.company.exception.ProjectException;
import com.ben.company.service.job.JobBaseService;
import com.ben.company.service.project.ProjectBaseService;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.company.constant.CommonConstant.*;
import static com.ben.company.constant.ProjectConstant.SELECT_PROJECT_ID;
import static com.ben.company.constant.TeamConstant.SELECT_TEAM_ID;
import static com.ben.company.enums.ProjectEnum.*;

/**
 * @author lomofu
 * @date 2020/2/28 21:28
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "project-svc")
public class ProjectBaseServiceImpl implements ProjectBaseService {
  public static final String DELETE_PROJECT_MAPPING_ACCOUNT_BY_PROJECT_ID =
      "deleteProjectMappingAccountByProjectId";

  public static final String DELETE_PROJECT_TABLE_BY_PROJECT_ID = "deleteProjectTableByProjectId";

  public static final String DELETE_PROJECT_MAPPING_ACCOUNT_BY_ACCOUNT_ID =
      "deleteProjectMappingAccountByAccountId";

  public static final String DELETE_PROJECT_BY_TEAM_ID = "deleteProjectByTeamId";

  @Resource private ProjectMapper projectMapper;

  @Resource private ProjectMappingAccountMapper projectMappingAccountMapper;

  @Resource private JobBaseService jobBaseService;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName)",
      condition =
          "#token != null && #token.length()>0 && #companyId!=null && #companyId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public int countProjectsByCompanyId(String token, String companyId) {
    return projectMapper.selectCountProjectByCompanyId(companyId);
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#projectId)",
      condition =
          "#token != null && #token.length()>0 && #projectId!=null && #projectId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public int countMembersByProjectId(String token, String projectId) {
    return projectMappingAccountMapper.selectCountByExample(
        Example.builder(ProjectMappingAccount.class)
            .where(Sqls.custom().andEqualTo(SELECT_PROJECT_ID, projectId))
            .build());
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#projectId)",
      condition =
          "#token != null && #token.length()>0 && #projectId!=null && #projectId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public Project findProjectByProjectId(String token, String projectId) {
    return projectMapper.selectOneByExample(
        Example.builder(Project.class)
            .where(Sqls.custom().andEqualTo(SELECT_ID, projectId).andEqualTo(SELECT_ACTIVE, true))
            .build());
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#teamId)",
      condition = "#token != null && #token.length()>0 && #teamId!=null && #teamId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public List<Project> findProjectListByTeamId(String token, String teamId) {
    return Optional.ofNullable(
            projectMapper
                .selectByExample(
                    Example.builder(Project.class)
                        .select()
                        .where(
                            Sqls.custom()
                                .andEqualTo(SELECT_TEAM_ID, teamId)
                                .andEqualTo(SELECT_ACTIVE, true))
                        .build())
                .stream())
        .map(
            e ->
                e.sorted(Comparator.comparing(Project::getCreateTime).reversed())
                    .collect(Collectors.toList()))
        .orElseThrow(() -> new ProjectException(WITHOUT_RES));
  }

  @Override
  @Cacheable(
      key =
          "#token.concat(':page:').concat(#root.methodName).concat(#pageFilter.getSortBy())"
              + ".concat(#pageFilter.isSortDesc()).concat(#pageFilter.getPageSize())",
      condition =
          "#token!=null && #token.length()>0 && #pageFilter!=null "
              + "&& (#pageFilter.getPageNumber() == 1 || #pageFilter.getPageNumber()==2) "
              + "&& (#pageFilter.getPageSize()==5 || #pageFilter.getPageSize()==10 || #pageFilter.getPageSize()==50 )",
      unless = "#result == null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public PageInfo<Object> findProjectListByCompanyIdWithPageFilter(
      String token, PageFilter<String> pageFilter) {
    return PageHelper.startPage(
            pageFilter.getPageNumber(),
            pageFilter.getPageSize(),
            PageFilter.sortByPageFilter(pageFilter))
        .doSelectPageInfo(() -> projectMapper.selectProjectListByCompanyId(pageFilter.getData()));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteProjectMappingAccountByProjectId(String token, String projectId) {
    String key = DELETE_PROJECT_MAPPING_ACCOUNT_BY_PROJECT_ID.concat(projectId);
    try {
      locker.lock(key, 10L);
      projectMappingAccountMapper.deleteByExample(
          Example.builder(ProjectMappingAccount.class)
              .andWhere(Sqls.custom().andEqualTo(SELECT_PROJECT_ID, projectId))
              .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(MAPPING_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteProjectTableByProjectId(String token, String projectId) {
    String key = DELETE_PROJECT_TABLE_BY_PROJECT_ID.concat(projectId);
    try {
      locker.lock(key, 10L);
      projectMapper.updateByPrimaryKeySelective(
          Project.builder().id(projectId).active(false).updateTime(new Date()).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(PROJECT_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int deleteProjectMappingAccountByAccountId(String token, String accountId) {
    String key = DELETE_PROJECT_MAPPING_ACCOUNT_BY_ACCOUNT_ID.concat(accountId);
    try {
      locker.lock(key, 10L);
      return projectMappingAccountMapper.deleteByExample(
          Example.builder(ProjectMappingAccount.class)
              .andWhere(Sqls.custom().andEqualTo(SELECT_ACCOUNT_ID, accountId))
              .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(MAPPING_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
        @CacheEvict(key = "#token.concat(':countProjectsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findProjectByProjectId')", allEntries = true),
        @CacheEvict(key = "#token.concat(':findProjectListByTeamId:'.concat(#teamId))"),
      })
  @Transactional(rollbackFor = Exception.class)
  public void deleteProjectByTeamId(String token, String teamId) {
    String key = DELETE_PROJECT_BY_TEAM_ID.concat(teamId);
    try {
      locker.lock(key, 10L);
      projectMapper
          .selectByExample(
              Example.builder(Project.class)
                  .where(
                      Sqls.custom()
                          .andEqualTo(SELECT_TEAM_ID, teamId)
                          .andEqualTo(SELECT_ACTIVE, true))
                  .build())
          .forEach(e -> this.deleteProjectByProjectId(token, e.getId()));
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(PROJECT_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
        @CacheEvict(key = "#token.concat(':countProjectsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findProjectByProjectId:').concat(#projectId)"),
        @CacheEvict(key = "#token.concat(':findProjectListByTeamId')", allEntries = true),
      })
  @Transactional(rollbackFor = Exception.class)
  public void deleteProjectByProjectId(String token, String projectId) {
    try {
      // 删除 project 表
      CompletableFuture.runAsync(
              () -> this.deleteProjectTableByProjectId(token, projectId),
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
                throw new ProjectException(PROJECT_DELETE_FAIL);
              });

      // 删除 projectMappingAccount 表
      CompletableFuture.runAsync(
              () -> this.deleteProjectMappingAccountByProjectId(token, projectId),
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
                throw new ProjectException(MAPPING_DELETE_FAIL);
              });

      // 删除project下的所有任务
      CompletableFuture.runAsync(
              () -> jobBaseService.deleteJobByProjectId(token, projectId),
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
                throw new ProjectException(PROJECT_DELETE_FAIL);
              });
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(PROJECT_DELETE_FAIL);
    }
  }
}
