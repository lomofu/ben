package com.ben.company.controller;

import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import com.ben.company.service.job.JobService;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.job.NewJobVO;
import com.ben.company.vo.job.UpdateJobVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;
import static com.ben.common.constant.PermissionConstant.*;

/**
 * @author lomofu
 * @date 2020/2/14 15:11
 */
@Api(tags = "任务接口")
@RestController
@RequestMapping(path = "/api/jobs")
@Validated
public class JobController {

  @Resource private JobService jobService;

  @GetMapping(path = "/jobListByProjectId")
  @CheckPerm(value = SYS_JOB_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取项目的任务列表")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "项目id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<JobResponse> getJobListByProjectId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> jobService.getJobListByProjectId(token, id));
  }

  @GetMapping(path = "/thisWeekJobListByAccountId")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取账号的本周任务")
  public WebAsyncTask<JobResponse> getThisWeekJobListByAccountId(PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> jobService.getThisWeekJobListByAccountId(pageFilter));
  }

  @GetMapping(path = "/jobListByAccountId")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取账号的所有任务")
  public WebAsyncTask<JobResponse> getJobListByAccountId(PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> jobService.getJobListByAccountId(pageFilter));
  }

  @GetMapping(path = "/job")
  @CheckPerm(value = SYS_JOB_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取单个任务的详情")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "id",
        value = "任务id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<JobResponse> getJobById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> jobService.getJobById(token, id));
  }

  @GetMapping("/totalJobCount")
  @CheckPerm(value = SYS_PROJECT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取任务统计")
  public WebAsyncTask<JobResponse> getTotalJobCountByProjectId(
      @RequestParam @NotBlank(message = PARAM_MISSING) String projectId) {
    return new WebAsyncTask<>(() -> jobService.getTotalJobCountByProjectId(projectId));
  }

  @PostMapping(path = "/job")
  @CheckPerm(value = SYS_JOB_ADD)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 创建一个任务")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "newJobVO",
        value = "newJobVO 新建任务的视图类",
        dataType = "NewJobVO",
        required = true),
  })
  public WebAsyncTask<JobResponse> createNewJob(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull NewJobVO newJobVO) {
    return new WebAsyncTask<>(() -> jobService.createNewJob(token, newJobVO));
  }

  @PutMapping(path = "/job")
  @CheckPerm(value = SYS_JOB_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 修改任务信息")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "updateJobVO",
        value = "updateJobVO 视图类",
        dataType = "NewJobVO",
        required = true),
  })
  public WebAsyncTask<JobResponse> updateJob(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid UpdateJobVO updateJobVO) {
    return new WebAsyncTask<>(() -> jobService.updateJob(token, updateJobVO));
  }

  @DeleteMapping(path = "/job/{jobId}")
  @CheckPerm(value = SYS_JOB_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 删除任务")
  @ApiImplicitParams({
    @ApiImplicitParam(
        paramType = "header",
        name = "from",
        value = "来着网关请求需要网关id",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "service",
        value = "服务调用者描述",
        dataType = "String"),
    @ApiImplicitParam(
        paramType = "header",
        name = "Authorization",
        value = "token令牌",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "path",
        name = "jobId",
        value = "任务id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<JobResponse> deleteJobById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank(message = PARAM_MISSING) String jobId) {
    return new WebAsyncTask<>(() -> jobService.deleteJobById(token, jobId));
  }
}
