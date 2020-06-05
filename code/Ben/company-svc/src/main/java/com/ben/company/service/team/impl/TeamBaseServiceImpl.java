package com.ben.company.service.team.impl;

import com.ben.common.annotation.CacheExpire;
import com.ben.common.domain.PageFilter;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.TeamMapper;
import com.ben.company.dao.TeamMappingAccountMapper;
import com.ben.company.domain.Team;
import com.ben.company.domain.TeamMappingAccount;
import com.ben.company.exception.TeamException;
import com.ben.company.service.project.ProjectBaseService;
import com.ben.company.service.team.TeamBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.company.constant.CommonConstant.*;
import static com.ben.company.constant.CompanyConstant.SELECT_COMPANY_ID;
import static com.ben.company.constant.TeamConstant.SELECT_TEAM_ID;
import static com.ben.company.enums.TeamEnum.*;

/**
 * @author lomofu
 * @date 2020/2/28 16:25
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "team-svc")
public class TeamBaseServiceImpl implements TeamBaseService {
  public static final String INSERT_ACCOUNT_ID_AND_TEAM_ID_MAPPING =
      "insertAccountIdAndTeamIdMapping";

  public static final String DELETE_TEAM_TABLE_BY_TEAM_ID = "deleteTeamTableByTeamId";

  public static final String DELETE_TEAM_MAPPING_ACCOUNT_BY_TEAM_ID =
      "deleteTeamMappingAccountByTeamId";

  public static final String DELETE_TEAM_MAPPING_ACCOUNT_BY_ACCOUNT_ID =
      "deleteTeamMappingAccountByAccountId";

  @Resource private TeamMapper teamMapper;

  @Resource private TeamMappingAccountMapper teamMappingAccountMapper;

  @Resource private ProjectBaseService projectBaseService;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName)",
      condition =
          "#token != null && #token.length()>0 && #companyId!=null && #companyId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public int countTeamsByCompanyId(String token, String companyId) {
    return teamMapper.selectCountByExample(
        Example.builder(Team.class)
            .where(
                Sqls.custom()
                    .andEqualTo(SELECT_COMPANY_ID, companyId)
                    .andEqualTo(SELECT_ACTIVE, true))
            .build());
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName).concat(':').concat(#teamId)",
      condition = "#token != null && #token.length()>0 && #teamId!=null && #teamId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public Team findTeamByTeamId(String token, String teamId) {
    return teamMapper.selectOneByExample(
        Example.builder(Team.class)
            .where(Sqls.custom().andEqualTo(SELECT_ID, teamId).andEqualTo(SELECT_ACTIVE, true))
            .build());
  }

  @Override
  public List<Team> findTeamListByAccountId(String token, String accountId) {
    return teamMapper.selectTeamListByAccountId(accountId);
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName)",
      condition =
          "#token != null && #token.length()>0 && #accountId!=null && #accountId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public List<Team> findSimpleTeamListByAccountId(String token, String accountId) {
    return teamMapper.selectSimpleTeamListByAccountId(accountId);
  }

  @Override
  @Cacheable(
      key =
          "#token.concat(':page:').concat(#root.methodName).concat(':').concat(#pageFilter.getSortBy())"
              + ".concat(#pageFilter.isSortDesc()).concat(#pageFilter.getPageSize())",
      condition =
          "#token!=null && #token.length()>0 && #pageFilter!=null "
              + "&& (#pageFilter.getPageNumber() == 1 || #pageFilter.getPageNumber()==2) "
              + "&& (#pageFilter.getPageSize()==5 || #pageFilter.getPageSize()==10 || #pageFilter.getPageSize()==50 )",
      unless = "#result == null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public PageInfo<Object> findTeamListByCompanyIdWithPageFilter(
      String token, PageFilter<String> pageFilter) {
    return PageHelper.startPage(
            pageFilter.getPageNumber(),
            pageFilter.getPageSize(),
            PageFilter.sortByPageFilter(pageFilter))
        .doSelectPageInfo(() -> teamMapper.selectTeamListByCompanyId(pageFilter.getData()));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int insertAccountIdAndTeamIdMapping(String accountId, String teamId) {
    String key = INSERT_ACCOUNT_ID_AND_TEAM_ID_MAPPING.concat(accountId).concat(teamId);
    try {
      locker.lock(key, 10L);
      return teamMappingAccountMapper.insert(
          TeamMappingAccount.builder().accountId(accountId).teamId(teamId).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new TeamException(MAPPING_INSERT_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTeamTableByTeamId(String teamId) {
    String key = DELETE_TEAM_TABLE_BY_TEAM_ID.concat(teamId);
    try {
      locker.lock(key, 10L);
      teamMapper.updateByPrimaryKeySelective(
          Team.builder().id(teamId).active(false).updateTime(new Date()).build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new TeamException(TEAM_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTeamMappingAccountByTeamId(String teamId) {
    String key = DELETE_TEAM_MAPPING_ACCOUNT_BY_TEAM_ID.concat(teamId);
    try {
      locker.lock(key, 10L);
      teamMappingAccountMapper.deleteByExample(
          Example.builder(TeamMappingAccount.class)
              .where(Sqls.custom().andEqualTo(SELECT_TEAM_ID, teamId))
              .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new TeamException(MAPPING_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int deleteTeamMappingAccountByAccountId(String accountId) {
    String key = DELETE_TEAM_MAPPING_ACCOUNT_BY_ACCOUNT_ID.concat(accountId);
    try {
      locker.lock(key, 10L);
      return teamMappingAccountMapper.deleteByExample(
          Example.builder(TeamMappingAccount.class)
              .where(Sqls.custom().andEqualTo(SELECT_ACCOUNT_ID, accountId))
              .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new TeamException(MAPPING_INSERT_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @CacheEvict(
      cacheNames = "account-svc",
      key = "#token.concat(':findSimpleTeamListByAccountId:').concat(id)",
      allEntries = true)
  public void deleteTeamByTeamId(String token, String id) {
    try {
      // team 表
      CompletableFuture.runAsync(
              () -> this.deleteTeamTableByTeamId(id), appConfiguration.getAsyncExecutor())
          .exceptionally(
              e -> {
                log.error(
                    METHOD_ERROR_TEMPLATE,
                    Thread.currentThread().getName(),
                    this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    e.getMessage());
                throw new TeamException(TEAM_DELETE_FAIL);
              });
      // team mapping 表
      CompletableFuture.runAsync(
              () -> this.deleteTeamMappingAccountByTeamId(id), appConfiguration.getAsyncExecutor())
          .exceptionally(
              e -> {
                log.error(
                    METHOD_ERROR_TEMPLATE,
                    Thread.currentThread().getName(),
                    this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    e.getMessage());
                throw new TeamException(MAPPING_DELETE_FAIL);
              });
      // project 表
      CompletableFuture.runAsync(
              () -> projectBaseService.deleteProjectByTeamId(token, id),
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
                throw new TeamException(TEAM_DELETE_FAIL);
              });
    } catch (Exception e) {
      throw new TeamException(TEAM_DELETE_FAIL);
    }
  }
}
