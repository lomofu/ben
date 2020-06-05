package com.ben.company.service.project.impl;

import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.enums.ResultCode;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.ProjectMapper;
import com.ben.company.dao.ProjectMappingAccountMapper;
import com.ben.company.domain.Project;
import com.ben.company.domain.ProjectMappingAccount;
import com.ben.company.domain.Team;
import com.ben.company.dto.project.ProjectDto;
import com.ben.company.exception.ProjectException;
import com.ben.company.service.project.ProjectBaseService;
import com.ben.company.service.project.ProjectService;
import com.ben.company.service.team.TeamBaseService;
import com.ben.company.vo.project.*;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ben.common.enums.ResultCode.PARAM_MISS;
import static com.ben.company.constant.CommonConstant.*;
import static com.ben.company.constant.ProjectConstant.SELECT_PROJECT_ID;
import static com.ben.company.constant.TeamConstant.SELECT_TEAM_ID;
import static com.ben.company.enums.ProjectEnum.*;

/**
 * @author lomofu
 * @date 2020/1/27 20:23
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "project-svc")
public class ProjectServiceImpl implements ProjectService {
  public static final String INSERT_ACCOUNT_LIST_TO_PROJECT_MAPPING =
      "insertAccountListToProjectMapping";

  public static final String CREATE_NEW_PROJECT = "createNewProject";

  public static final String UPDATE_PROJECT = "updateProject";

  public static final String DELETE_ACCOUNT_LIST_TO_PROJECT_MAPPING =
      "deleteAccountListToProjectMapping";

  @Resource private ProjectMapper projectMapper;

  @Resource private TeamBaseService teamBaseService;

  @Resource private ProjectBaseService projectBaseService;

  @Resource private ProjectMappingAccountMapper projectMappingAccountMapper;

  @Resource private RedissonDistributedLocker locker;

  @Resource private AppConfiguration appConfiguration;

  @Override
  public ProjectResponse getProjectById(String token, String id) {
    try {
      CompletableFuture<ProjectDto> projectDtoCompletableFuture =
          CompletableFuture.supplyAsync(
                  () ->
                      Optional.ofNullable(projectBaseService.findProjectByProjectId(token, id))
                          .map(
                              v -> {
                                Team team =
                                    Optional.ofNullable(
                                            teamBaseService.findTeamByTeamId(token, v.getTeamId()))
                                        .orElseThrow(() -> new ProjectException(WITHOUT_RES));
                                ProjectDto projectDto = new ProjectDto();
                                BeanUtils.copyProperties(v, projectDto);
                                return projectDto.toBuilder().teamName(team.getName()).build();
                              })
                          .orElseThrow(() -> new ProjectException(PROJECT_IS_EMPTY)),
                  appConfiguration.getAsyncExecutor())
              .exceptionally(
                  e -> {
                    throw new CustomException(e.getMessage());
                  });

      CompletableFuture<Integer> totalFuture =
          CompletableFuture.supplyAsync(
                  () -> projectBaseService.countMembersByProjectId(token, id),
                  appConfiguration.getAsyncExecutor())
              .exceptionally(
                  e -> {
                    throw new CustomException(e.getMessage());
                  });

      return new ProjectResponse(
          projectDtoCompletableFuture.join().toBuilder().total(totalFuture.join()).build());
    } catch (Exception e) {
      throw new ProjectException(PROJECT_IS_EMPTY);
    }
  }

  @Override
  public ProjectResponse getProjectListByTeamId(String token, String id) {
    return Optional.ofNullable(projectBaseService.findProjectListByTeamId(token, id))
        .map(ProjectResponse::new)
        .orElseThrow(() -> new ProjectException(WITHOUT_RES));
  }

  @Override
  public ProjectResponse listProjectsByCompanyId(String token, PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();
    return Optional.ofNullable(
            projectBaseService.findProjectListByCompanyIdWithPageFilter(token, filter))
        .map(
            e -> PageVO.builder().pages(e.getPages()).total(e.getTotal()).list(e.getList()).build())
        .map(ProjectResponse::new)
        .orElseThrow(() -> new ProjectException(WITHOUT_RES));
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(
            key =
                "#token.concat(':countMembersByProjectId:'.concat(#accountListToProjectMappingVO.getProjectId()))"),
        @CacheEvict(
            cacheNames = "account-svc",
            key = "#token.concat(':page:').concat('findAccountListByProjectId')",
            allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public ProjectResponse insertAccountListToProjectMapping(
      String token, AccountListToProjectMappingVO<String> accountListToProjectMappingVO) {
    AccountListToProjectMappingVO<String> projectMappingVO =
        Optional.ofNullable(accountListToProjectMappingVO)
            .orElseThrow(() -> new CustomException(PARAM_MISS));
    if (projectMappingVO.getList().isEmpty()) {
      throw new ProjectException(EMPLOYEE_LIST_EMPTY);
    }
    boolean match =
        projectMappingVO.getList().stream()
            .map(
                e ->
                    projectMappingAccountMapper.selectOneByExample(
                        Example.builder(ProjectMappingAccount.class)
                            .where(
                                Sqls.custom()
                                    .andEqualTo(SELECT_PROJECT_ID, projectMappingVO.getProjectId())
                                    .andEqualTo(SELECT_ACCOUNT_ID, e))
                            .build()))
            .noneMatch(Objects::isNull);
    if (match) {
      throw new ProjectException(SOMEONE_HAS_EXIST_ON_THE_LIST);
    }
    String key = INSERT_ACCOUNT_LIST_TO_PROJECT_MAPPING.concat(projectMappingVO.getProjectId());
    try {
      locker.lock(key, 10L);
      projectMappingVO
          .getList()
          .forEach(
              e ->
                  projectMappingAccountMapper.insert(
                      ProjectMappingAccount.builder()
                          .accountId(e)
                          .projectId(projectMappingVO.getProjectId())
                          .build()));
      return new ProjectResponse(EMPLOYEE_ADD_PROJECT_SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(EMPLOYEE_ADD_PROJECT_FAIL);
    } finally {
      assert locker != null;
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
        @CacheEvict(key = "#token.concat(':countProjectsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findProjectListByTeamId')", allEntries = true),
      })
  @Transactional(rollbackFor = Exception.class)
  public ProjectResponse createNewProject(String token, NewProjectVO newProjectVO) {
    Project project =
        Optional.ofNullable(newProjectVO)
            .map(
                e ->
                    Project.builder()
                        .name(e.getName())
                        .description(e.getDescription())
                        .teamId(e.getTeamId())
                        .createTime(new Date())
                        .updateTime(new Date())
                        .active(true)
                        .build())
            .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));
    if (this.isHaveSameProjectName(newProjectVO.getName(), newProjectVO.getTeamId())) {
      throw new ProjectException(PROJECT_NAME_EXISTED);
    }
    String key = CREATE_NEW_PROJECT.concat(project.getTeamId());
    try {
      locker.lock(key, 10L);
      int insert = projectMapper.insert(project);
      int insert1 =
          projectMappingAccountMapper.insert(
              ProjectMappingAccount.builder()
                  .projectId(project.getId())
                  .accountId(newProjectVO.getAccountId())
                  .build());
      if (insert > 0 && insert1 > 0) {
        return new ProjectResponse(PROJECT_CREATE_SUCCESS, project.getId());
      }
      throw new ProjectException(PROJECT_CREATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(PROJECT_CREATE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
        @CacheEvict(
            key = "#token.concat(':findProjectByProjectId:').concat(#updateProjectVO.getId())"),
        @CacheEvict(
            key = "#token.concat(':findProjectListByTeamId').concat(#updateProjectVO.getTeamId())"),
      })
  @Transactional(rollbackFor = Exception.class)
  public ProjectResponse updateProject(String token, UpdateProjectVO updateProjectVO) {
    Project project =
        Optional.ofNullable(updateProjectVO)
            .map(
                e ->
                    Project.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .description(e.getDescription())
                        .updateTime(new Date())
                        .build())
            .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));

    if (isModified(project)
        && this.isHaveSameProjectName(project.getName(), updateProjectVO.getTeamId())) {
      throw new ProjectException(PROJECT_NAME_EXISTED);
    }
    String key = UPDATE_PROJECT.concat(project.getId());
    try {
      locker.lock(key, 10L);
      int update = projectMapper.updateByPrimaryKeySelective(project);
      if (update > 0) {
        return new ProjectResponse(PROJECT_UPDATE_SUCCESS, project.getId());
      }
      throw new ProjectException(PROJECT_UPDATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CustomException(e.getMessage());
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':page')", allEntries = true),
        @CacheEvict(
            key =
                "#token.concat(':findProjectByProjectId:').concat(#updateProjectToTeamVO.getId())"),
        @CacheEvict(
            key =
                "#token.concat(':findProjectListByTeamId').concat(#updateProjectToTeamVO.getTeamId())"),
      })
  public ProjectResponse updateProjectToOtherTeam(
      String token, UpdateProjectToTeamVO updateProjectToTeamVO) {
    Project project =
        Optional.ofNullable(updateProjectToTeamVO)
            .map(e -> Project.builder().id(e.getId()).teamId(e.getTeamId()).build())
            .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));
    String key = "updateProjectToOtherTeam".concat(project.getId()).concat(project.getTeamId());
    try {
      locker.lock(key, 10L);
      int update = projectMapper.updateByPrimaryKeySelective(project);
      if (update > 0) {
        return new ProjectResponse(PROJECT_CHANGE_TEAM_SUCCESS);
      }
      throw new ProjectException(PROJECT_CHANGE_TEAM_FAIL);
    } catch (Exception e) {
      throw new ProjectException(PROJECT_CHANGE_TEAM_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public ProjectResponse deleteProjectById(String token, String id) {
    try {
      projectBaseService.deleteProjectByProjectId(token, id);
      return new ProjectResponse(PROJECT_DELETE_SUCCESS);
    } catch (Exception e) {
      throw new ProjectException(PROJECT_DELETE_FAIL);
    }
  }

  @Override
  public ProjectResponse deleteProjectListByProjectListId(
      String token, List<String> projectListId) {
    try {
      projectListId.forEach(e -> projectBaseService.deleteProjectByProjectId(token, e));
      if (projectListId.size() > 1) {
        return new ProjectResponse(PROJECT_LIST_DELETE_SUCCESS);
      }
      return new ProjectResponse(PROJECT_DELETE_SUCCESS);
    } catch (Exception e) {
      if (projectListId.size() > 1) {
        throw new ProjectException(PROJECT_LIST_DELETE_FAIL);
      }
      throw new ProjectException(PROJECT_DELETE_FAIL);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':countMembersByProjectId:'.concat(#projectId))"),
        @CacheEvict(
            cacheNames = "account-svc",
            key = "#token.concat(':page:').concat('findAccountListByProjectId')",
            allEntries = true),
        @CacheEvict(
            cacheNames = "account-svc",
            key = "#token.concat(':page:').concat('findSimpleAccountListByProjectId')",
            allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public ProjectResponse deleteAccountListToProjectMapping(
      String token, String projectId, List<String> list) {
    String key = DELETE_ACCOUNT_LIST_TO_PROJECT_MAPPING.concat(projectId);
    try {
      locker.lock(key, 10L);
      list.forEach(
          e ->
              projectMappingAccountMapper.delete(
                  ProjectMappingAccount.builder().projectId(projectId).accountId(e).build()));
      return new ProjectResponse(PROJECT_REMOVE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new ProjectException(PROJECT_REMOVE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  protected boolean isHaveSameProjectName(String projectName, String teamId) {
    return Optional.ofNullable(
            projectMapper.selectOneByExample(
                Example.builder(Project.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_NAME, projectName)
                            .andEqualTo(SELECT_TEAM_ID, teamId)
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .isPresent();
  }

  protected boolean isModified(Project project) {
    return !Optional.ofNullable(
            projectMapper.selectOneByExample(
                Example.builder(Team.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_ID, project.getId())
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .map(e -> e.getName().equals(project.getName()))
        .orElseThrow(() -> new CustomException(ResultCode.PARAM_MISS));
  }
}
