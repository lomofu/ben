package com.ben.company.service.project;

import com.ben.common.domain.PageFilter;
import com.ben.company.vo.project.*;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/27 20:23
 */
public interface ProjectService {
  ProjectResponse getProjectById(String token, String id);

  ProjectResponse getProjectListByTeamId(String token, String id);

  ProjectResponse listProjectsByCompanyId(String token, PageFilter<String> pageFilter);

  ProjectResponse createNewProject(String token, NewProjectVO newProjectVO);

  ProjectResponse insertAccountListToProjectMapping(
      String token, AccountListToProjectMappingVO<String> accountListToProjectMappingVO);

  ProjectResponse updateProject(String token, UpdateProjectVO updateProjectVO);

  ProjectResponse updateProjectToOtherTeam(
      String token, UpdateProjectToTeamVO updateProjectToTeamVO);

  ProjectResponse deleteProjectById(String token, String id);

  ProjectResponse deleteProjectListByProjectListId(String token, List<String> projectListId);

  ProjectResponse deleteAccountListToProjectMapping(
      String token, String projectId, List<String> list);
}
