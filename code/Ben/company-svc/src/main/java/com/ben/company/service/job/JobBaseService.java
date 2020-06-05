package com.ben.company.service.job;

import com.ben.common.domain.PageFilter;
import com.ben.company.domain.Job;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/2/29 01:12
 */
public interface JobBaseService {

  List<Job> findJobListByProjectId(String token, String projectId);

  PageInfo<Object> findJobListByAccountId(PageFilter<String> pageFilter);

  PageInfo<Object> findJobListByAccountIdThisWeek(PageFilter<String> filter);

  Job findJobByJobId(String token, String jobId);

  void deleteJobTableByJobId(String jobId);

  void deleteJobMappingAccountByJobId(String jobId);

  int deleteJobMappingAccountByAccountId(String accountId);

  void deleteJobByProjectId(String token, String projectId);

  void deleteJobByJobId(String token, String id);
}
