package com.ben.company.service.team;

import com.ben.common.domain.PageFilter;
import com.ben.company.domain.Team;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/2/28 16:24
 */
public interface TeamBaseService {

  int countTeamsByCompanyId(String token, String companyId);

  Team findTeamByTeamId(String token, String teamId);

  List<Team> findTeamListByAccountId(String token, String accountId);

  List<Team> findSimpleTeamListByAccountId(String token, String accountId);

  PageInfo<Object> findTeamListByCompanyIdWithPageFilter(
      String token, PageFilter<String> pageFilter);

  int insertAccountIdAndTeamIdMapping(String accountId, String teamId);

  int deleteTeamMappingAccountByAccountId(String accountId);

  void deleteTeamTableByTeamId(String teamId);

  void deleteTeamMappingAccountByTeamId(String teamId);

  void deleteTeamByTeamId(String token, String teamId);
}
