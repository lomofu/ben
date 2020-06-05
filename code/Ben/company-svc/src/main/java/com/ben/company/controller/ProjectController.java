package com.ben.company.controller;

import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import com.ben.company.service.project.ProjectService;
import com.ben.company.vo.project.*;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_HEADER;
import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;
import static com.ben.common.constant.PermissionConstant.*;

/**
 * @author lomofu
 * @date 2020/1/27 20:19
 */
@Api(tags = "项目接口")
@RestController
@RequestMapping(path = "/api/projects")
@Validated
public class ProjectController {

  @Resource private ProjectService projectService;

  @GetMapping(path = "/projectListByTeamId")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取团队的项目列表")
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
        value = "团队id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> getProjectListByTeamId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> projectService.getProjectListByTeamId(token, id));
  }

  @GetMapping(path = "/project")
  @CheckPerm(value = SYS_PROJECT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取项目信息")
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
  public WebAsyncTask<ProjectResponse> getProjectById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> projectService.getProjectById(token, id));
  }

  @PostMapping(path = "/project")
  @CheckPerm(value = SYS_PROJECT_ADD)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 创建新项目")
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
        name = "newProjectVO",
        value = "newProjectVO 视图类",
        dataType = "NewProjectVO",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> createNewProject(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull NewProjectVO newProjectVO) {
    return new WebAsyncTask<>(() -> projectService.createNewProject(token, newProjectVO));
  }

  @PutMapping(path = "/project")
  @CheckPerm(value = SYS_PROJECT_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 更新项目")
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
        name = "updateProjectVO",
        value = "updateProjectVO 视图类",
        dataType = "UpdateProjectVO",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> updateProject(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull UpdateProjectVO updateProjectVO) {
    return new WebAsyncTask<>(() -> projectService.updateProject(token, updateProjectVO));
  }

  @DeleteMapping(path = "/project/{id}")
  @CheckPerm(value = SYS_PROJECT_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 删除项目")
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
        name = "id",
        value = "项目id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> deleteProject(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> projectService.deleteProjectById(token, id));
  }

  @PutMapping(path = "/projectByTeamId")
  @CheckPerm(value = SYS_PROJECT_CHANGE)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 交换项目去别的团队")
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
        name = "updateProjectToTeamVO",
        value = "updateProjectToTeamVO 视图类",
        dataType = "UpdateProjectToTeamVO",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> updateProjectToOtherTeam(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull UpdateProjectToTeamVO updateProjectToTeamVO) {
    return new WebAsyncTask<>(
        () -> projectService.updateProjectToOtherTeam(token, updateProjectToTeamVO));
  }

  @GetMapping(path = "/projectList")
  @CheckPerm(value = SYS_PROJECT_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 获取公司所有项目列表")
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
        name = "pageFilter",
        value = "pageFilter 分页过滤器",
        dataType = "PageFilter<String>",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> listProjectsByCompanyId(
      @RequestHeader(AUTHORIZATION_HEADER) String token, PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> projectService.listProjectsByCompanyId(token, pageFilter));
  }

  @DeleteMapping(path = "/projectList")
  @CheckPerm(value = SYS_PROJECT_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除项目")
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
        name = "projectListId",
        value = "projectListId 项目列表",
        dataType = "List<String>",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> deleteProjectsByProjectListId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @NotEmpty List<String> projectListId) {
    return new WebAsyncTask<>(
        () -> projectService.deleteProjectListByProjectListId(token, projectListId));
  }

  @PostMapping(path = "/projectMapping")
  @CheckPerm(value = SYS_PROJECT_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量加入员工到项目中")
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
        name = "accountListToProjectMappingVO",
        value = "accountListToProjectMappingVO 视图类",
        dataType = "AccountListToProjectMappingVO<String>",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> insertAccountListToProjectMapping(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull
          AccountListToProjectMappingVO<String> accountListToProjectMappingVO) {
    return new WebAsyncTask<>(
        () ->
            projectService.insertAccountListToProjectMapping(token, accountListToProjectMappingVO));
  }

  @DeleteMapping(path = "/projectMapping/{projectId}")
  @CheckPerm(value = SYS_PROJECT_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除项目员工")
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
        name = "projectId",
        value = "项目id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "list",
        value = "账户id列表",
        dataType = "List<String>",
        required = true),
  })
  public WebAsyncTask<ProjectResponse> deleteAccountListToProjectMapping(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank String projectId,
      @RequestBody @NotEmpty List<String> list) {
    return new WebAsyncTask<>(
        () -> projectService.deleteAccountListToProjectMapping(token, projectId, list));
  }
}
