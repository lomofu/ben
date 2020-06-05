package com.ben.company.service.team;

import com.ben.common.domain.PageFilter;
import com.ben.company.vo.team.AccountListToTeamMappingVO;
import com.ben.company.vo.team.NewTeamVO;
import com.ben.company.vo.team.TeamResponse;
import com.ben.company.vo.team.UpdateTeamVO;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/26 23:03
 */
public interface TeamService {
  TeamResponse countTeamsByCompanyId(String token, String companyId);

  TeamResponse listTeamsByCompanyId(String token, PageFilter<String> pageFilter);

  TeamResponse listAllTeamsByAccountId(String token, String accountId);

  TeamResponse getLeftNavByAccountId(String token, String accountId);

  TeamResponse createNewTeam(String token, NewTeamVO newTeamVO);

  TeamResponse insertAccountListToTeamMapping(
      String token, AccountListToTeamMappingVO<String> accountListToTeamMappingVO);

  TeamResponse updateTeam(String token, UpdateTeamVO updateTeamVO);

  TeamResponse deleteTeamById(String token, String id);

  TeamResponse deleteTeamListByTeamListId(String token, List<String> teamListId);

  TeamResponse deleteAccountListToTeamMapping(
      String token, String teamId, boolean active, List<String> list);

  TeamResponse isUnique(String teamName);
}
