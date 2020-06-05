package com.ben.company.controller;

import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import com.ben.company.service.search.SearchService;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.project.ProjectResponse;
import com.ben.company.vo.team.TeamResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.constant.PermissionConstant.SYS_PROJECT_VIEW;
import static com.ben.common.constant.PermissionConstant.SYS_TEAM_VIEW;

/**
 * @author lomofu
 * @date 2020/3/13 20:36
 */
@Api(tags = "搜索接口")
@RestController
@RequestMapping("/api/search")
@Valid
public class SearchController {
  @Resource private SearchService searchService;

  @GetMapping("/team")
  @CheckPerm(value = SYS_TEAM_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 搜索团队")
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
        name = "search",
        value = "搜索内容",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<TeamResponse> searchTeam(
      PageFilter<String> pageFilter, @NotBlank String search) {
    return new WebAsyncTask<>(() -> searchService.searchTeam(pageFilter, search));
  }

  @GetMapping("/project")
  @CheckPerm(value = SYS_PROJECT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 搜索项目")
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
        name = "search",
        value = "搜索内容",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true)
  })
  public WebAsyncTask<ProjectResponse> searchProject(
      PageFilter<String> pageFilter, @NotBlank String search) {
    return new WebAsyncTask<>(() -> searchService.searchProject(pageFilter, search));
  }

  @GetMapping(value = "/teamMonthLine")
  @CheckPerm(value = SYS_TEAM_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取团队每月创建团队总数")
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
        name = "companyId",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<TeamResponse> getMonthDaysTeamLineData(@RequestParam String companyId) {
    return new WebAsyncTask<>(() -> searchService.getMonthOfThisYearTeamLineData(companyId));
  }

  @GetMapping(value = "/projectMonthLine")
  @CheckPerm(value = SYS_PROJECT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取团队每月创建项目总数")
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
        name = "companyId",
        value = "公司id",
        dataType = "String",
        required = true)
  })
  public WebAsyncTask<ProjectResponse> getMonthDaysProjectsLineData(
      @RequestParam String companyId) {
    return new WebAsyncTask<>(() -> searchService.getMonthDaysProjectLineData(companyId));
  }

  @GetMapping(value = "/thisWeekJobsLine")
  public WebAsyncTask<JobResponse> getThisWeekJobsLineData(@RequestParam String projectId) {
    return new WebAsyncTask<>(() -> searchService.getThisWeekJobsLineData(projectId));
  }
}
