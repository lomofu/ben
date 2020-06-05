package com.ben.account.controller;

import com.ben.account.service.SearchService;
import com.ben.account.vo.AccountResponse;
import com.ben.common.annotation.Authorize;
import com.ben.common.annotation.CheckPerm;
import com.ben.common.domain.PageFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;

import static com.ben.common.constant.AuthConstant.AUTHORIZATION_SERVICE_GATEWAY;
import static com.ben.common.constant.PermissionConstant.SYS_EMPLOYEE_VIEW;

/**
 * @author lomofu
 * @date 2020/3/12 20:48
 */
@Api(tags = "账户微服务搜索接口")
@RestController
@RequestMapping("/api/search")
public class SearchController {
  @Resource private SearchService searchService;

  @GetMapping("/account/company")
  @CheckPerm(SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 搜索公司中账户")
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
        paramType = "query",
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "search",
        value = "搜索关键字",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<AccountResponse> findAccountByCompanyId(
      PageFilter<String> pageFilter, String search) {
    return new WebAsyncTask<>(() -> searchService.findAccountByCompanyId(pageFilter, search));
  }

  @GetMapping("/account/team")
  @CheckPerm(SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 搜索团队中账户")
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
        paramType = "query",
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "search",
        value = "搜索关键字",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<AccountResponse> findAccountByTeamId(
      PageFilter<String> pageFilter, String search) {
    return new WebAsyncTask<>(() -> searchService.findAccountByTeamId(pageFilter, search));
  }

  @GetMapping("/account/project")
  @CheckPerm(SYS_EMPLOYEE_VIEW)
  @Authorize(value = AUTHORIZATION_SERVICE_GATEWAY)
  @ApiOperation("异步请求: 搜索项目中账户")
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
        paramType = "query",
        name = "pageFilter",
        value = "分页过滤器",
        dataType = "PageFilter<String>",
        required = true),
    @ApiImplicitParam(
        paramType = "query",
        name = "search",
        value = "搜索关键字",
        dataType = "String",
        required = true),
  })
  public WebAsyncTask<AccountResponse> findAccountByProjectId(
      PageFilter<String> pageFilter, String search) {
    return new WebAsyncTask<>(() -> searchService.findAccountByProjectId(pageFilter, search));
  }
}
