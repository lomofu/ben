package com.ben.company.service.search.impl;

import com.ben.common.domain.PageFilter;
import com.ben.common.domain.PageVO;
import com.ben.common.utils.TimeHelper;
import com.ben.company.domain.EsJob;
import com.ben.company.domain.EsProject;
import com.ben.company.domain.EsTeam;
import com.ben.company.dto.search.LineChartDto;
import com.ben.company.repository.JobEsRepository;
import com.ben.company.repository.ProjectEsRepository;
import com.ben.company.repository.TeamEsRepository;
import com.ben.company.service.search.SearchService;
import com.ben.company.utils.PageFilterForJpaHelper;
import com.ben.company.vo.job.JobResponse;
import com.ben.company.vo.project.ProjectResponse;
import com.ben.company.vo.team.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.ben.company.constant.SearchConstant.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author lomofu
 * @date 2020/3/13 20:37
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
  @Resource private ElasticsearchTemplate elasticsearchTemplate;

  @Resource private TeamEsRepository teamEsRepository;

  @Resource private ProjectEsRepository projectEsRepository;

  @Resource private JobEsRepository jobEsRepository;

  @Override
  public TeamResponse searchTeam(PageFilter<String> pageFilter, String search) {
    SearchQuery searchQuery = this.getSearchQuery(pageFilter, search);
    return Optional.of(elasticsearchTemplate.queryForPage(searchQuery, EsTeam.class))
        .map(
            e ->
                PageVO.<EsTeam>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(e.toList())
                    .build())
        .map(TeamResponse::new)
        .orElseGet(TeamResponse::new);
  }

  @Override
  public ProjectResponse searchProject(PageFilter<String> pageFilter, String search) {
    SearchQuery searchQuery = this.getSearchQuery(pageFilter, search);
    return Optional.of(elasticsearchTemplate.queryForPage(searchQuery, EsProject.class))
        .map(
            e ->
                PageVO.<EsProject>builder()
                    .pages(e.getTotalPages())
                    .total(e.getTotalElements())
                    .list(e.toList())
                    .build())
        .map(ProjectResponse::new)
        .orElseGet(ProjectResponse::new);
  }

  @Override
  public TeamResponse getMonthOfThisYearTeamLineData(String companyId) {
    SearchQuery searchQuery = this.getSearchQuery(TEAM_MONTH_CHART_DATA, companyId);
    AggregatedPage<EsTeam> search = (AggregatedPage<EsTeam>) teamEsRepository.search(searchQuery);
    Histogram histogram = search.getAggregations().get(TEAM_MONTH_CHART_DATA);
    return new TeamResponse(
        histogram.getBuckets().stream()
            .map(
                e -> LineChartDto.builder().date(e.getKeyAsString()).count(e.getDocCount()).build())
            .collect(Collectors.toList()));
  }

  @Override
  public ProjectResponse getMonthDaysProjectLineData(String companyId) {
    SearchQuery searchQuery = this.getSearchQuery(PROJECT_MONTH_CHART_DATA, companyId);
    AggregatedPage<EsProject> search =
        (AggregatedPage<EsProject>) projectEsRepository.search(searchQuery);
    Histogram histogram = search.getAggregations().get(PROJECT_MONTH_CHART_DATA);
    return new ProjectResponse(
        histogram.getBuckets().stream()
            .map(
                e -> LineChartDto.builder().date(e.getKeyAsString()).count(e.getDocCount()).build())
            .collect(Collectors.toList()));
  }

  @Override
  public JobResponse getThisWeekJobsLineData(String projectId) {
    String startBound = TimeHelper.converseDateToString(TimeHelper.nowWeekStart());
    String endBound = TimeHelper.converseDateToString(TimeHelper.nowWeekEnd());

    DateHistogramAggregationBuilder aggregationBuilder =
        AggregationBuilders.dateHistogram(JOB_WEEK_LINE_DATA)
            .field(START)
            .timeZone(DateTimeZone.forTimeZone(TimeZone.getDefault()))
            .dateHistogramInterval(DateHistogramInterval.DAY)
            .format(DAY_FORMAT)
            .minDocCount(0)
            .extendedBounds(
                new ExtendedBounds(
                    TimeHelper.converseSimpleDateToString(TimeHelper.nowWeekStart()),
                    TimeHelper.converseSimpleDateToString(TimeHelper.nowWeekEnd())));

    NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
            .withQuery(
                boolQuery()
                    .must(termQuery(PROJECT_ID, projectId))
                    .must(termQuery(ACTIVE, true))
                    .must(rangeQuery(START).gte(startBound).lte(endBound).format(YYYY_MM_DD_HH_MM)))
            .addAggregation(aggregationBuilder)
            .build();

    AggregatedPage<EsJob> search = (AggregatedPage<EsJob>) jobEsRepository.search(searchQuery);
    Histogram histogram = search.getAggregations().get(JOB_WEEK_LINE_DATA);
    return new JobResponse(
        histogram.getBuckets().stream()
            .map(
                e -> LineChartDto.builder().date(e.getKeyAsString()).count(e.getDocCount()).build())
            .collect(Collectors.toList()));
  }

  protected SearchQuery getSearchQuery(PageFilter<String> pageFilter, String search) {
    return new NativeSearchQueryBuilder()
        .withQuery(
            boolQuery()
                .must(termQuery(COMPANY_ID, pageFilter.getData()))
                .must(termQuery(ACTIVE, true))
                .must(
                    boolQuery()
                        .should(QueryBuilders.matchQuery(NAME, search))
                        .should(QueryBuilders.wildcardQuery(NAME, search))
                        .should(QueryBuilders.wildcardQuery(NAME, this.getWildQuery(search)))
                        .should(QueryBuilders.matchQuery(ID, search))
                        .should(QueryBuilders.wildcardQuery(ID, search))
                        .should(QueryBuilders.wildcardQuery(ID, this.getWildQuery(search)))
                        .should(QueryBuilders.matchQuery(DESCRIPTION, search))
                        .should(
                            QueryBuilders.wildcardQuery(DESCRIPTION, this.getWildQuery(search)))))
        .withPageable(PageFilterForJpaHelper.getJpaPageRequest(pageFilter))
        .build();
  }

  protected String getWildQuery(String search) {
    String query = QueryParser.escape(search);
    return ASTERISK.concat(query).concat(ASTERISK);
  }

  protected SearchQuery getSearchQuery(String aggName, String companyId) {
    LocalDate now = LocalDate.now();
    String startBound = String.valueOf(now.getYear()).concat("-01");
    String endBound =
        String.valueOf(now.getYear()).concat("-").concat(String.valueOf(now.getMonthOfYear()));

    DateHistogramAggregationBuilder aggregationBuilder =
        AggregationBuilders.dateHistogram(aggName)
            .field(CREATE_TIME)
            .timeZone(DateTimeZone.forTimeZone(TimeZone.getDefault()))
            .dateHistogramInterval(DateHistogramInterval.MONTH)
            .format(MONTH_FORMAT)
            .minDocCount(0)
            .extendedBounds(new ExtendedBounds(startBound, endBound));

    return new NativeSearchQueryBuilder()
        .withQuery(boolQuery().must(termQuery(COMPANY_ID, companyId)).must(termQuery(ACTIVE, true)))
        .addAggregation(aggregationBuilder)
        .build();
  }
}
