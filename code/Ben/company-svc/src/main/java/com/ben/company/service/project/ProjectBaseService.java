package com.ben.company.service.project;

import com.ben.common.domain.PageFilter;
import com.ben.company.domain.Project;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/2/28 21:27
 */
public interface ProjectBaseService {
  int countProjectsByCompanyId(String token, String companyId);

  int countMembersByProjectId(String token, String projectId);

  Project findProjectByProjectId(String token, String projectId);

  List<Project> findProjectListByTeamId(String token, String teamId);

  PageInfo<Object> findProjectListByCompanyIdWithPageFilter(
      String token, PageFilter<String> pageFilter);

  void deleteProjectTableByProjectId(String token, String projectId);

  void deleteProjectMappingAccountByProjectId(String token, String projectId);

  int deleteProjectMappingAccountByAccountId(String token, String accountId);

  void deleteProjectByTeamId(String token, String teamId);

  void deleteProjectByProjectId(String token, String projectId);
}
