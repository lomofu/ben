package com.ben.company.utils;

import com.ben.common.domain.PageFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static com.ben.common.constant.CommonConstant.UTILITY_CLASS;
import static com.ben.company.constant.SearchConstant.SORT_DEFAULT_VALUE;

/**
 * @author lomofu
 * @date 2020/2/15 01:05
 */
public class PageFilterForJpaHelper {

  private PageFilterForJpaHelper() {
    throw new IllegalStateException(UTILITY_CLASS);
  }

  public static PageRequest getJpaPageRequest(PageFilter<String> pageFilter) {
    PageFilter<String> filter = pageFilter.doFilter();
    Sort sort;
    if (filter.isSortDesc()) {
      sort =
          Sort.by(Sort.Direction.DESC, SORT_DEFAULT_VALUE)
              .and(Sort.by(Sort.Direction.DESC, pageFilter.getSortBy()));
    } else {
      sort =
          Sort.by(Sort.Direction.DESC, SORT_DEFAULT_VALUE)
              .and(Sort.by(Sort.Direction.ASC, pageFilter.getSortBy()));
    }
    return PageRequest.of(filter.getPageNumber() - 1, filter.getPageSize(), sort);
  }
}
