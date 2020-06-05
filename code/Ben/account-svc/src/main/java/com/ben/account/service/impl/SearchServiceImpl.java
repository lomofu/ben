package com.ben.account.service.impl;

import com.ben.account.domain.EsAccount;
import com.ben.account.domain.EsAccountForProject;
import com.ben.account.domain.EsAccountForTeam;
import com.ben.account.dto.EmployeeDto;
import com.ben.account.service.SearchService;
import com.ben.account.utils.PageFilterForJpaHelper;
import com.ben.account.vo.AccountResponse;
import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ben.account.constant.SearchConstant.*;

/**
 * @author lomofu
 * @date 2020/3/12 20:46
 */
@Service
public class SearchServiceImpl implements SearchService {

  @Resource private ElasticsearchTemplate elasticsearchTemplate;

  @Override
  public AccountResponse findAccountByCompanyId(PageFilter<String> pageFilter, String search) {
    SearchQuery searchQuery = this.getSearchQuery(pageFilter, COMPANY_ID, search);
    return Optional.of(elasticsearchTemplate.queryForPage(searchQuery, EsAccount.class))
        .map(
            e ->
                PageVO.<EmployeeDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.getEmployeeDtoList(e.get()))
                    .build())
        .map(AccountResponse::new)
        .orElseGet(AccountResponse::new);
  }

  @Override
  public AccountResponse findAccountByTeamId(PageFilter<String> pageFilter, String search) {
    SearchQuery searchQuery = this.getSearchQuery(pageFilter, TEAM_ID, search);
    return Optional.of(elasticsearchTemplate.queryForPage(searchQuery, EsAccountForTeam.class))
        .map(
            e ->
                PageVO.<EmployeeDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.getEmployeeDtoListForTeam(e.get()))
                    .build())
        .map(AccountResponse::new)
        .orElseGet(AccountResponse::new);
  }

  @Override
  public AccountResponse findAccountByProjectId(PageFilter<String> pageFilter, String search) {
    SearchQuery searchQuery = this.getSearchQuery(pageFilter, PROJECT_ID, search);
    return Optional.of(elasticsearchTemplate.queryForPage(searchQuery, EsAccountForProject.class))
        .map(
            e ->
                PageVO.<EmployeeDto>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(this.getEmployeeDtoListForProject(e.get()))
                    .build())
        .map(AccountResponse::new)
        .orElseGet(AccountResponse::new);
  }

  protected SearchQuery getSearchQuery(PageFilter<String> pageFilter, String limit, String search) {
    return new NativeSearchQueryBuilder()
        .withQuery(
            QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(limit, pageFilter.getData()))
                .must(QueryBuilders.termQuery(IS_ACTIVE, true))
                .must(
                    QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery(NAME, search))
                        .should(QueryBuilders.wildcardQuery(NAME, search))
                        .should(QueryBuilders.wildcardQuery(NAME, this.getWildQuery(search)))
                        .should(QueryBuilders.matchQuery(EMAIL, search))
                        .should(QueryBuilders.wildcardQuery(EMAIL, search))
                        .should(QueryBuilders.wildcardQuery(EMAIL, this.getWildQuery(search)))
                        .should(QueryBuilders.matchQuery(PHONE_NUMBER, search))
                        .should(QueryBuilders.wildcardQuery(PHONE_NUMBER, search))
                        .should(
                            QueryBuilders.wildcardQuery(PHONE_NUMBER, this.getWildQuery(search)))
                        .should(QueryBuilders.matchQuery(ID, search))
                        .should(QueryBuilders.wildcardQuery(ID, search))
                        .should(QueryBuilders.wildcardQuery(ID, this.getWildQuery(search)))))
        .withPageable(PageFilterForJpaHelper.getJpaPageRequest(pageFilter))
        .build();
  }

  protected String getWildQuery(String search) {
    String query = QueryParser.escape(search);
    return ASTERISK.concat(query).concat(ASTERISK);
  }

  protected List<EmployeeDto> getEmployeeDtoList(Stream<EsAccount> e) {
    return e.map(
            i ->
                EmployeeDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .email(i.getEmail())
                    .phoneNumber(i.getPhoneNumber())
                    .avatarUrl(i.getAvatarUrl())
                    .gender(3)
                    .isActive(i.isActive())
                    .isAdmin(i.isAdmin())
                    .createTime(i.getCreateTime())
                    .build())
        .collect(Collectors.toList());
  }

  protected List<EmployeeDto> getEmployeeDtoListForTeam(Stream<EsAccountForTeam> e) {
    return e.map(
            i ->
                EmployeeDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .email(i.getEmail())
                    .phoneNumber(i.getPhoneNumber())
                    .avatarUrl(i.getAvatarUrl())
                    .gender(3)
                    .isActive(i.isActive())
                    .isAdmin(i.isAdmin())
                    .createTime(i.getCreateTime())
                    .build())
        .collect(Collectors.toList());
  }

  protected List<EmployeeDto> getEmployeeDtoListForProject(Stream<EsAccountForProject> e) {
    return e.map(
            i ->
                EmployeeDto.builder()
                    .id(i.getId())
                    .name(i.getName())
                    .email(i.getEmail())
                    .phoneNumber(i.getPhoneNumber())
                    .avatarUrl(i.getAvatarUrl())
                    .gender(3)
                    .isActive(i.isActive())
                    .isAdmin(i.isAdmin())
                    .createTime(i.getCreateTime())
                    .build())
        .collect(Collectors.toList());
  }
}
