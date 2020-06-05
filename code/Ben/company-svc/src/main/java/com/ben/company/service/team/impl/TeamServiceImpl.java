package com.ben.company.service.team.impl;

import com.ben.account.client.AccountClient;
import com.ben.account.vo.AccountResponse;
import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.exception.CustomException;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.dao.TeamMapper;
import com.ben.company.dao.TeamMappingAccountMapper;
import com.ben.company.domain.Project;
import com.ben.company.domain.Team;
import com.ben.company.domain.TeamMappingAccount;
import com.ben.company.dto.project.LeftNavProjectDto;
import com.ben.company.enums.ProjectEnum;
import com.ben.company.exception.ProjectException;
import com.ben.company.exception.TeamException;
import com.ben.company.service.project.ProjectBaseService;
import com.ben.company.service.team.TeamBaseService;
import com.ben.company.service.team.TeamService;
import com.ben.company.vo.member.MemberVO;
import com.ben.company.vo.project.LeftNavProjectVO;
import com.ben.company.vo.team.*;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_COMPANY;
import static com.ben.common.enums.ResultCode.PARAM_MISS;
import static com.ben.common.enums.ResultCode.SUCCESS;
import static com.ben.company.constant.CommonConstant.*;
import static com.ben.company.constant.CompanyConstant.SELECT_COMPANY_ID;
import static com.ben.company.constant.TeamConstant.SELECT_TEAM_ID;
import static com.ben.company.enums.TeamEnum.*;

/**
 * @author lomofu
 * @date 2020/1/26 23:08
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "team-svc")
public class TeamServiceImpl implements TeamService {
  public static final String INSERT_ACCOUNT_LIST_TO_TEAM_MAPPING = "insertAccountListToTeamMapping";

  public static final String UPDATE_TEAM = "updateTeam";

  public static final String DELETE_ACCOUNT_LIST_TO_TEAM_MAPPING = "deleteAccountListToTeamMapping";

  public static final String CREATE_NEW_TEAM = "createNewTeam";

  @Resource private TeamMapper teamMapper;

  @Resource private TeamMappingAccountMapper teamMappingAccountMapper;

  @Resource private TeamBaseService teamBaseService;

  @Resource private ProjectBaseService projectBaseService;

  @Resource private AccountClient accountClient;

  @Resource private RedissonDistributedLocker locker;

  @Override
  public TeamResponse countTeamsByCompanyId(String token, String companyId) {
    int teamCount = teamBaseService.countTeamsByCompanyId(token, companyId);
    if (teamCount < 0) {
      throw new TeamException(WITHOUT_RES);
    }
    return new TeamResponse(teamCount);
  }

  @Override
  public TeamResponse listTeamsByCompanyId(String token, PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();

    return Optional.ofNullable(teamBaseService.findTeamListByCompanyIdWithPageFilter(token, filter))
        .map(
            e -> PageVO.builder().pages(e.getPages()).total(e.getTotal()).list(e.getList()).build())
        .map(TeamResponse::new)
        .orElseThrow(() -> new TeamException(WITHOUT_RES));
  }

  @Override
  public TeamResponse listAllTeamsByAccountId(String token, String accountId) {
    return Optional.of(
            teamBaseService.findSimpleTeamListByAccountId(token, accountId).stream()
                .map(e -> SimpleTeamListVO.builder().id(e.getId()).name(e.getName()).build())
                .sorted(Comparator.comparing(SimpleTeamListVO::getName))
                .collect(Collectors.toList()))
        .map(TeamResponse::new)
        .orElseThrow(() -> new TeamException(WITHOUT_RES));
  }

  @Override
  public TeamResponse getLeftNavByAccountId(String token, String accountId) {
    List<Team> teamList =
        Optional.ofNullable(teamBaseService.findTeamListByAccountId(token, accountId))
            .orElseThrow(() -> new TeamException(WITHOUT_RES));

    List<LeftNavTeamVO> leftNavTeamVOList =
        teamList.stream()
            .map(
                e ->
                    LeftNavTeamVO.builder()
                        .teamId(e.getId())
                        .teamName(e.getName())
                        .description(e.getDescription())
                        .createTime(e.getCreateTime())
                        .updateTime(e.getUpdateTime())
                        .build())
            .sorted(Comparator.comparing(LeftNavTeamVO::getCreateTime).reversed())
            .collect(Collectors.toList());
    return new TeamResponse(converseToTreeVO(token, leftNavTeamVOList));
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':countTeamsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':findSimpleTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public TeamResponse createNewTeam(String token, NewTeamVO newTeamVO) {
    NewTeamVO vo =
        Optional.ofNullable(newTeamVO).orElseThrow(() -> new CustomException(PARAM_MISS));

    if (Boolean.TRUE.equals(this.isHaveSameTeamName(vo.getName(), vo.getCompanyId()))) {
      throw new TeamException(TEAM_NAME_EXISTED);
    }
    Team team =
        Team.builder()
            .name(vo.getName())
            .description(vo.getDescription())
            .companyId(vo.getCompanyId())
            .createTime(new Date())
            .updateTime(new Date())
            .active(true)
            .build();

    String key = CREATE_NEW_TEAM.concat(team.getCompanyId());
    try {
      locker.lock(key, 10L);
      int insert = teamMapper.insert(team);
      int insert1 =
          teamMappingAccountMapper.insert(
              TeamMappingAccount.builder()
                  .teamId(team.getId())
                  .accountId(vo.getAccountId())
                  .build());
      if (insert > 0 && insert1 > 0) {
        return new TeamResponse(TEAM_CREATE_SUCCESS, team.getId());
      }
      throw new TeamException(TEAM_CREATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CustomException(e.getMessage());
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public TeamResponse insertAccountListToTeamMapping(
      String token, AccountListToTeamMappingVO<String> accountListToTeamMappingVO) {
    AccountListToTeamMappingVO<String> listToTeamMappingVO =
        Optional.ofNullable(accountListToTeamMappingVO)
            .orElseThrow(() -> new CustomException(PARAM_MISS));
    if (listToTeamMappingVO.getList().size() <= 0) {
      throw new TeamException((EMPLOYEE_LIST_EMPTY));
    }
    boolean match =
        listToTeamMappingVO.getList().stream()
            .map(
                e ->
                    teamMappingAccountMapper.selectOneByExample(
                        Example.builder(TeamMappingAccount.class)
                            .where(
                                Sqls.custom()
                                    .andEqualTo(SELECT_TEAM_ID, listToTeamMappingVO.getTeamId())
                                    .andEqualTo(SELECT_ACCOUNT_ID, e))
                            .build()))
            .noneMatch(Objects::isNull);

    if (match) {
      throw new TeamException(SOMEONE_HAS_EXIST_ON_THE_LIST);
    }
    String key = INSERT_ACCOUNT_LIST_TO_TEAM_MAPPING.concat(listToTeamMappingVO.getTeamId());
    try {
      locker.lock(key, 10L);
      listToTeamMappingVO
          .getList()
          .forEach(
              e ->
                  teamMappingAccountMapper.insert(
                      TeamMappingAccount.builder()
                          .accountId(e)
                          .teamId(listToTeamMappingVO.getTeamId())
                          .build()));
      return new TeamResponse(EMPLOYEE_ADD_TEAM_SUCCESS);
    } catch (Exception e) {
      throw new TeamException(EMPLOYEE_ADD_TEAM_FAIL);
    } finally {
      assert locker != null;
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findTeamByTeamId:'.concat(#updateTeamVO.getId()))"),
        @CacheEvict(key = "#token.concat(':findTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':findSimpleTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public TeamResponse updateTeam(String token, UpdateTeamVO updateTeamVO) {
    Team team =
        Optional.ofNullable(updateTeamVO)
            .map(
                e ->
                    Team.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .description(e.getDescription())
                        .updateTime(new Date())
                        .build())
            .orElseThrow(() -> new CustomException(PARAM_MISS));

    if (isModified(team, updateTeamVO)
        && this.isHaveSameTeamName(updateTeamVO.getName(), updateTeamVO.getCompanyId())) {
      throw new TeamException(TEAM_NAME_EXISTED);
    }

    String key = UPDATE_TEAM.concat(team.getId());
    try {
      locker.lock(key, 10L);
      int update = teamMapper.updateByPrimaryKeySelective(team);
      if (update > 0) {
        return new TeamResponse(TEAM_UPDATE_SUCCESS, updateTeamVO.getId());
      }
      throw new TeamException(TEAM_UPDATE_FAIL);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CustomException(e.getMessage());
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findTeamByTeamId:'.concat(#id))"),
        @CacheEvict(key = "#token.concat(':countTeamsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findTeamListByAccountId')"),
        @CacheEvict(
            key = "#token.concat(':findAccountListByTeamId:').concat(#id)",
            allEntries = true),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true)
      })
  public TeamResponse deleteTeamById(String token, String id) {
    try {
      teamBaseService.deleteTeamByTeamId(token, id);
      return new TeamResponse(TEAM_DELETE_SUCCESS);
    } catch (Exception e) {
      throw new TeamException(TEAM_DELETE_FAIL);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(key = "#token.concat(':findTeamByTeamId')", allEntries = true),
        @CacheEvict(key = "#token.concat(':countTeamsByAccountId')"),
        @CacheEvict(key = "#token.concat(':findTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':findSimpleTeamListByAccountId')"),
        @CacheEvict(key = "#token.concat(':page')", allEntries = true)
      })
  @Transactional(rollbackFor = Exception.class)
  public TeamResponse deleteTeamListByTeamListId(String token, List<String> teamListId) {
    try {
      teamListId.forEach(e -> teamBaseService.deleteTeamByTeamId(token, e));
      if (teamListId.size() > 1) {
        return new TeamResponse(TEAM_LIST_DELETE_SUCCESS);
      }
      return new TeamResponse(TEAM_DELETE_SUCCESS);
    } catch (Exception e) {
      if (teamListId.size() > 1) {
        throw new TeamException(TEAM_LIST_DELETE_FAIL);
      }
      throw new TeamException(TEAM_DELETE_FAIL);
    }
  }

  @Override
  @CacheEvict(
      cacheNames = "account-svc",
      key = "#token.concat(':page:').concat('findAccountListByTeamId:'.concat(#teamId))",
      allEntries = true)
  @GlobalTransactional(name = "delete-accountList-to-teamMapping", rollbackFor = Exception.class)
  public TeamResponse deleteAccountListToTeamMapping(
      String token, String teamId, boolean active, List<String> list) {
    String key = DELETE_ACCOUNT_LIST_TO_TEAM_MAPPING.concat(teamId);
    try {
      locker.lock(key, 30L);
      if (active) {
        list.forEach(
            e ->
                teamMappingAccountMapper.delete(
                    TeamMappingAccount.builder().teamId(teamId).accountId(e).build()));
      } else {
        AccountResponse response =
            accountClient.deleteTempEmployeeMappingAccount(AUTHORIZATION_SERVICE_COMPANY, list);
        if (SUCCESS.getCode() != response.getCode()) {
          throw new TeamException(TEAM_REMOVE_FAIL);
        }
      }
      return new TeamResponse(TEAM_REMOVE_SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new TeamException(TEAM_REMOVE_FAIL);
    } finally {
      locker.unlock(key);
    }
  }

  @Override
  public TeamResponse isUnique(String teamName) {
    return new TeamResponse(!this.isHaveSameTeamName(teamName));
  }

  protected List<LeftNavTeamVO> converseToTreeVO(
      String token, List<LeftNavTeamVO> leftNavTeamVOList) {
    return leftNavTeamVOList.stream()
        .map(
            e -> {
              List<Object> children = new ArrayList<>(2);

              // 项目
              List<Project> projectList =
                  Optional.ofNullable(
                          projectBaseService.findProjectListByTeamId(token, e.getTeamId()))
                      .orElseThrow(() -> new ProjectException(ProjectEnum.WITHOUT_RES));
              List<LeftNavProjectDto> leftNavProjectDtoList =
                  projectList.stream()
                      .map(
                          i ->
                              LeftNavProjectDto.builder()
                                  .projectId(i.getId())
                                  .projectName(i.getName())
                                  .description(i.getDescription())
                                  .createTime(i.getCreateTime())
                                  .updateTime(i.getUpdateTime())
                                  .build())
                      .sorted(Comparator.comparing(LeftNavProjectDto::getCreateTime).reversed())
                      .collect(Collectors.toList());
              LeftNavProjectVO leftNavProjectVO =
                  LeftNavProjectVO.builder().children(leftNavProjectDtoList).build();

              // 成员
              MemberVO memberVO = MemberVO.builder().member(null).build();

              children.add(leftNavProjectVO);
              children.add(memberVO);
              return e.toBuilder().children(children).build();
            })
        .collect(Collectors.toList());
  }

  protected boolean isHaveSameTeamName(String teamName) {
    return Optional.ofNullable(
            teamMapper.selectOneByExample(
                Example.builder(Team.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_NAME, teamName)
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .isPresent();
  }

  protected boolean isHaveSameTeamName(String teamName, String companyId) {
    return Optional.ofNullable(
            teamMapper.selectOneByExample(
                Example.builder(Team.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_NAME, teamName)
                            .andEqualTo(SELECT_COMPANY_ID, companyId)
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .isPresent();
  }

  protected boolean isModified(Team team, UpdateTeamVO updateTeamVO) {
    return !Optional.ofNullable(
            teamMapper.selectOneByExample(
                Example.builder(Team.class)
                    .where(
                        Sqls.custom()
                            .andEqualTo(SELECT_ID, team.getId())
                            .andEqualTo(SELECT_ACTIVE, true))
                    .build()))
        .map(e -> e.getName().equals(updateTeamVO.getName()))
        .orElseThrow(() -> new CustomException(PARAM_MISS));
  }
}
