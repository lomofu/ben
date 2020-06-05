package com.ben.company.service.job;

import com.ben.common.domain.PageFilter;
import com.ben.company.domain.Job;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.job.NewJobVO;
import com.ben.company.vo.job.UpdateJobVO;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/2/14 15:12
 */
public interface JobService {

  List<Job> getJobRangeByProjectId(String projectId);

  void updateJobPush(List<Job> list);

  JobResponse getJobListByProjectId(String token, String id);

  JobResponse getThisWeekJobListByAccountId(PageFilter<String> pageFilter);

  JobResponse getJobListByAccountId(PageFilter<String> pageFilter);

  JobResponse getJobById(String token, String id);

  JobResponse getTotalJobCountByProjectId(String projectId);

  JobResponse createNewJob(String token, NewJobVO newJobVO);

  JobResponse updateJob(String token, UpdateJobVO updateJobVO);

  JobResponse deleteJobById(String token, String jobId);
}
