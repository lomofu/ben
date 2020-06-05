package com.ben.company.controller;

import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import com.ben.company.service.team.TeamService;
import com.ben.company.vo.team.AccountListToTeamMappingVO;
import com.ben.company.vo.team.NewTeamVO;
import com.ben.company.vo.team.TeamResponse;
import com.ben.company.vo.team.UpdateTeamVO;
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
 * @date 2020/1/26 22:57
 */
@Api(tags = "团队接口")
@RestController
@RequestMapping(path = "/api/teams")
@Validated
public class TeamController {

  @Resource private TeamService teamService;

  @PostMapping(path = "/team")
  @CheckPerm(value = SYS_TEAM_ADD)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 创建新的团队")
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
        name = "newTeamVO",
        value = "newTeamVO 视图类",
        dataType = "NewTeamVO",
        required = true),
  })
  public WebAsyncTask<TeamResponse> createNewTeam(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull NewTeamVO newTeamVO) {
    return new WebAsyncTask<>(() -> teamService.createNewTeam(token, newTeamVO));
  }

  @PutMapping(path = "/team")
  @CheckPerm(value = SYS_TEAM_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 更新团队信息")
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
        name = "updateTeamVO",
        value = "updateTeamVO 视图类",
        dataType = "UpdateTeamVO",
        required = true),
  })
  public WebAsyncTask<TeamResponse> updateTeam(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull UpdateTeamVO updateTeamVO) {
    return new WebAsyncTask<>(() -> teamService.updateTeam(token, updateTeamVO));
  }

  @DeleteMapping(path = "/team/{id}")
  @CheckPerm(value = SYS_TEAM_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 删除团队")
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
        value = "团队id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<TeamResponse> deleteTeamById(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @PathVariable @NotBlank(message = PARAM_MISSING) String id) {
    return new WebAsyncTask<>(() -> teamService.deleteTeamById(token, id));
  }

  @GetMapping(path = "/teamList")
  @CheckPerm(value = SYS_TEAM_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 列出公司的所有团队")
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
        value = "pageFilter 过滤器",
        dataType = "PageFilter<String>",
        required = true),
  })
  public WebAsyncTask<TeamResponse> listTeamsByCompanyId(
      @RequestHeader(AUTHORIZATION_HEADER) String token, PageFilter<String> pageFilter) {
    return new WebAsyncTask<>(() -> teamService.listTeamsByCompanyId(token, pageFilter));
  }

  @DeleteMapping(path = "/teamList")
  @CheckPerm(value = SYS_TEAM_DEL)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量删除团队")
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
        name = "teamListId",
        value = "团队id列表",
        dataType = "List<String>",
        required = true),
  })
  public WebAsyncTask<TeamResponse> deleteTeamsByTeamListId(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @NotEmpty List<String> teamListId) {
    return new WebAsyncTask<>(() -> teamService.deleteTeamListByTeamListId(token, teamListId));
  }

  @GetMapping(path = "/teamAllList")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 查询账户所属团队列表")
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
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<TeamResponse> listAllTeamsByAccountId(
      @RequestHeader(AUTHORIZATION_HEADER) String token, String accountId) {
    return new WebAsyncTask<>(() -> teamService.listAllTeamsByAccountId(token, accountId));
  }

  @GetMapping(path = "/leftNav")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 查询获得左侧树形结构导航栏")
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
        name = "accountId",
        value = "账户id",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<TeamResponse> getLeftNav(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam @NotBlank(message = PARAM_MISSING) String accountId) {
    return new WebAsyncTask<>(() -> teamService.getLeftNavByAccountId(token, accountId));
  }

  @GetMapping(path = "/isUnique")
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 查询团队名称是否唯一")
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
        name = "teamName",
        value = "团队名称",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<TeamResponse> isUnique(@RequestParam @NotBlank String teamName) {
    return new WebAsyncTask<>(() -> teamService.isUnique(teamName));
  }

  @PostMapping(path = "/teamMapping")
  @CheckPerm(value = SYS_TEAM_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量插入账户到指定团队")
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
        name = "accountListToTeamMappingVO",
        value = "团队名称",
        dataType = "AccountListToTeamMappingVO<String>",
        required = true),
  })
  public WebAsyncTask<TeamResponse> insertAccountListToTeamMapping(
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestBody @Valid @NotNull AccountListToTeamMappingVO<String> accountListToTeamMappingVO) {
    return new WebAsyncTask<>(
        () -> teamService.insertAccountListToTeamMapping(token, accountListToTeamMappingVO));
  }

  @DeleteMapping(path = "/teamMapping/{teamId}")
  @CheckPerm(value = SYS_TEAM_EDIT)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 批量插入账户到指定团队")
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
        name = "teamId",
        value = "团队id",
        dataType = "String",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "active",
        value = "是否是激活的员工列表",
        dataType = "boolean",
        required = true),
    @ApiImplicitParam(
        paramType = "body",
        name = "list",
        value = "员工id列表",
        dataType = "List<String>",
        required = true),
  })
  public WebAsyncTask<TeamResponse> deleteAccountListToTeamMapping(
      @PathVariable @NotBlank String teamId,
      @RequestHeader(AUTHORIZATION_HEADER) String token,
      @RequestParam("active") boolean active,
      @RequestBody @NotEmpty List<String> list) {
    return new WebAsyncTask<>(
        () -> teamService.deleteAccountListToTeamMapping(token, teamId, active, list));
  }
}
