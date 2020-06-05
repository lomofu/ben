package com.ben.company.service.company.impl;

import com.ben.account.client.AccountClient;
import com.ben.account.dto.AccountDT;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.JwtHelper;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.CompanyMapper;
import com.ben.company.dao.CompanyMappingAccountMapper;
import com.ben.company.domain.Company;
import com.ben.company.domain.CompanyMappingAccount;
import com.ben.company.dto.company.CompanyDto;
import com.ben.company.dto.company.CompanyFeignDto;
import com.ben.company.dto.company.NewCompanyDto;
import com.ben.company.exception.CompanyException;
import com.ben.company.service.company.CompanyBaseService;
import com.ben.company.service.company.CompanyService;
import com.ben.company.service.job.JobBaseService;
import com.ben.company.service.project.ProjectBaseService;
import com.ben.company.service.team.TeamBaseService;
import com.ben.company.vo.company.CompanyResponse;
import com.ben.company.vo.company.TotalCountVO;
import com.ben.company.vo.company.UpdateCompanyVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;
import static com.ben.common.constant.AuthConstant.CLAIM_ID;
import static com.ben.common.constant.LogConstant.METHOD_ERROR_TEMPLATE;
import static com.ben.common.enums.ResultCode.PARAM_MISS;
import static com.ben.company.constant.CommonConstant.SELECT_ACCOUNT_ID;
import static com.ben.company.enums.CompanyEnum.*;

/**
 * @author lomofu
 * @date 2020/1/24 14:24
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "company-svc")
public class CompanyServiceImpl implements CompanyService {
  public static final String UPDATE_COMPANY = "updateCompany";

  public static final String CREATE_NEW_COMPANY = "createNewCompany";

  public static final String CREATE_EMPLOYEE_MAPPING_WITH_COMPANY =
      "createEmployeeMappingWithCompany";

  public static final String CREATE_EMPLOYEE_MAPPING_WITH_TEAM = "createEmployeeMappingWithTeam";

  public static final String DELETE_ACCOUNT_MAPPING = "deleteAccountMapping";

  @Resource private AccountClient accountClient;

  @Resource private CompanyMapper companyMapper;

  @Resource private CompanyMappingAccountMapper companyMappingAccountMapper;

  @Resource private CompanyBaseService companyBaseService;

  @Resource private TeamBaseService teamBaseService;

  @Resource private ProjectBaseService projectBaseService;

  @Resource private JobBaseService jobBaseService;

  @Resource private ObjectMapper objectMapper;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  public CompanyResponse getCompanyById(String token, String id) {
    return Optional.ofNullable(companyBaseService.findCompanyById(token, id))
        .map(CompanyResponse::new)
        .orElseThrow(() -> new CompanyException(COMPANY_IS_EMPTY));
  }

  @Override
  public CompanyResponse getCompanyByIdForFeign(String token, String id) {

    return Optional.ofNullable(companyBaseService.findCompanyById(token, id))
        .map(
            e -> {
              CompanyFeignDto companyFeignDto = new CompanyFeignDto();
              BeanUtils.copyProperties(e, companyFeignDto);
              return new CompanyResponse(companyFeignDto);
            })
        .orElseThrow(() -> new CompanyException(COMPANY_IS_EMPTY));
  }

  @Override
  public CompanyResponse getAccountAndCompanyByToken(String token) {
    try {
      String accountId = JwtHelper.verifyToken(token).get(CLAIM_ID).asString();
      CompletableFuture<CompanyResponse> companyResponseCompletableFuture =
          CompletableFuture.supplyAsync(
                  () ->
                      Optional.ofNullable(
                              accountClient.getAccountByIdForFeign(
                                  AUTHORIZATION_SERVICE_COMPANY, token, accountId))
                          .map(e -> objectMapper.convertValue(e.getData(), AccountDT.class))
                          .orElseThrow(() -> new CompanyException(WITHOUT_RES)),
                  appConfiguration.getAsyncExecutor())
              .thenCombine(
                  CompletableFuture.supplyAsync(
                      () ->
                          Optional.ofNullable(
                                  companyBaseService.findCompanyByAccountId(token, accountId))
                              .orElseThrow(() -> new CompanyException(COMPANY_IS_EMPTY)),
                      appConfiguration.getAsyncExecutor()),
                  (i, j) ->
                      new CompanyResponse(
                          CompanyDto.builder()
                              .id(j.getId())
                              .name(j.getName())
                              .description(j.getDescription())
                              .address(j.getAddress())
                              .accountId(i.getId())
                              .accountName(i.getName())
                              .gender(i.getGender())
                              .email(i.getEmail())
                              .phoneNumber(i.getPhoneNumber())
                              .avatarUrl(i.getAvatarUrl())
                              .createTime(i.getCreateTime())
                              .companyCreateTime(j.getCreateTime())
                              .companyUpdateTime(j.getUpdateTime())
                              .type(j.getType())
                              .admin(i.isAdmin())
                              .active(i.isActive())
                              .role(i.getRole())
                              .permissionList(i.getPermissionList())
                              .build()));
      return companyResponseCompletableFuture.join();
    } catch (Exception e) {
      throw new CompanyException(WITHOUT_RES);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':').concat('findCompanyById')"),
        @CacheEvict(key = "#token.concat(':').concat('findCompanyByAccountId')")
      })
  @Transactional(rollbackFor = Exception.class)
  public CompanyResponse updateCompany(String token, UpdateCompanyVO updateCompanyVO) {
    Company company =
        Optional.ofNullable(updateCompanyVO)
            .map(
                e ->
                    Company.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .description(e.getDescription())
                        .address(e.getAddress())
                        .updateTime(new Date())
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));
    String key = UPDATE_COMPANY.concat(company.getId());
    try {
      locker.lock(key, 10L);
      int update = companyMapper.updateByPrimaryKeySelective(company);
      if (update > 0) {
        return new CompanyResponse(COMPANY_UPDATE_SUCCESS);
      }
      throw new CompanyException(COMPANY_UPDATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CompanyException(COMPANY_UPDATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CompanyResponse createNewCompany(NewCompanyDto newCompanyDto) {
    Company company =
        Optional.ofNullable(newCompanyDto)
            .map(
                e ->
                    Company.builder()
                        .accountId(e.getAccountId())
                        .address(e.getCompanyAddress())
                        .description(null)
                        .type(e.getType())
                        .name(e.getCompanyName())
                        .createTime(new Date())
                        .updateTime(new Date())
                        .active(true)
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));

    String key = CREATE_NEW_COMPANY.concat(company.getAccountId());
    try {
      locker.lock(key, 15L);
      int insert = companyMapper.insert(company);

      int insert1 =
          companyMappingAccountMapper.insert(
              CompanyMappingAccount.builder()
                  .accountId(company.getAccountId())
                  .companyId(company.getId())
                  .build());
      if (insert > 0 && insert1 > 0) {
        return new CompanyResponse(COMPANY_CREATED_SUCCESS);
      }
      throw new CompanyException(COMPANY_CREATED_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CompanyException(COMPANY_CREATED_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public CompanyResponse getTotalCountByCompanyId(String token, String companyId) {
    // team 数量
    CompletableFuture<Integer> teamCountFuture =
        CompletableFuture.supplyAsync(
            () -> teamBaseService.countTeamsByCompanyId(token, companyId),
            appConfiguration.getAsyncExecutor());
    // project 数量
    CompletableFuture<Integer> projectCountFuture =
        CompletableFuture.supplyAsync(
            () -> projectBaseService.countProjectsByCompanyId(token, companyId),
            appConfiguration.getAsyncExecutor());
    // member 数量
    CompletableFuture<Integer> memberCountFuture =
        CompletableFuture.supplyAsync(
            () -> companyBaseService.countMembersInCompanyByCompanyId(token, companyId),
            appConfiguration.getAsyncExecutor());
    return new CompanyResponse(
        TotalCountVO.builder()
            .teamCount(teamCountFuture.join())
            .projectCount(projectCountFuture.join())
            .memberCount(memberCountFuture.join())
            .build());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @CacheEvict(key = "#companyId.concat(':').concat('countMembersInCompanyByCompanyId')")
  public CompanyResponse createEmployeeMappingWithCompany(String accountId, String companyId) {
    String key = CREATE_EMPLOYEE_MAPPING_WITH_COMPANY.concat(accountId).concat(companyId);
    try {
      locker.lock(key, 10L);
      int insert =
          companyMappingAccountMapper.insert(
              CompanyMappingAccount.builder().companyId(companyId).accountId(accountId).build());
      if (insert > 0) {
        return new CompanyResponse(MAPPING_SUCCESS);
      }
      throw new CompanyException(MAPPING_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CompanyException(MAPPING_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CompanyResponse createEmployeeMappingWithTeam(String accountId, String teamId) {
    String key = CREATE_EMPLOYEE_MAPPING_WITH_TEAM.concat(accountId).concat(teamId);
    try {
      locker.lock(key, 10L);
      int insert = teamBaseService.insertAccountIdAndTeamIdMapping(accountId, teamId);
      if (insert > 0) {
        return new CompanyResponse(MAPPING_SUCCESS);
      }
      throw new CompanyException(MAPPING_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CompanyException(MAPPING_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = {Exception.class, Error.class})
  @CacheEvict(key = "#token.concat(':').concat('countMembersInCompanyByCompanyId')")
  public CompanyResponse deleteAccountMapping(String token, String id) {
    String key = DELETE_ACCOUNT_MAPPING.concat(id);
    try {
      locker.lock(key, 30L);
      // 异步删除 公司与账户的映射关系
      CompletableFuture<Integer> companyFuture =
          CompletableFuture.supplyAsync(
                  () ->
                      companyMappingAccountMapper.deleteByExample(
                          Example.builder(CompanyMappingAccount.class)
                              .where(Sqls.custom().andEqualTo(SELECT_ACCOUNT_ID, id))
                              .build()),
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
                    throw new CompanyException(ACCOUNT_DELETE_FAIL);
                  });

      // 异步删除 团队与账户的映射关系
      CompletableFuture<Integer> teamFuture =
          CompletableFuture.supplyAsync(
                  () -> teamBaseService.deleteTeamMappingAccountByAccountId(id),
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
                    throw new CompanyException(ACCOUNT_DELETE_FAIL);
                  });

      // 异步删除 项目与账户的映射关系
      CompletableFuture<Integer> projectFuture =
          CompletableFuture.supplyAsync(
                  () -> projectBaseService.deleteProjectMappingAccountByAccountId(token, id),
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
                    throw new CompanyException(ACCOUNT_DELETE_FAIL);
                  });

      CompletableFuture<Integer> jobFuture =
          CompletableFuture.supplyAsync(
                  () -> jobBaseService.deleteJobMappingAccountByAccountId(id),
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
                    throw new CompanyException(ACCOUNT_DELETE_FAIL);
                  });
      if (companyFuture.join() > 0
          && teamFuture.join() > -1
          && jobFuture.join() > -1
          && projectFuture.join() > -1) {
        return new CompanyResponse(MAPPING_DELETE_SUCCESS);
      }
      throw new CompanyException(MAPPING_DELETE_FAIL);
    } catch (Exception e) {
      throw new CompanyException(ACCOUNT_DELETE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }
}
