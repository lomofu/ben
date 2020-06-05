package com.ben.company.service.search;

import com.ben.common.domain.PageFilter;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.project.ProjectResponse;
import com.ben.company.vo.team.TeamResponse;

/**
 * @author lomofu
 * @date 2020/3/13 20:36
 */
public interface SearchService {
  TeamResponse searchTeam(PageFilter<String> pageFilter, String search);

  ProjectResponse searchProject(PageFilter<String> pageFilter, String search);

  TeamResponse getMonthOfThisYearTeamLineData(String companyId);

  ProjectResponse getMonthDaysProjectLineData(String companyId);

  JobResponse getThisWeekJobsLineData(String projectId);
}
