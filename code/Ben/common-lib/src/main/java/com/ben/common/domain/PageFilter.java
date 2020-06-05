package com.ben.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author lomofu
 * @date 2020/2/7 17:15
 */
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class PageFilter<T> {
  @ApiModelProperty(value = "当前页数")
  private int pageNumber;

  @ApiModelProperty(value = "分页大小")
  private int pageSize;

  @ApiModelProperty(value = "根据排序的字段")
  private String sortBy;

  @ApiModelProperty(value = "是否降序")
  private boolean sortDesc;

  @ApiModelProperty(value = "分页请求携带的参数")
  private T data;

  public static String sortByPageFilter(PageFilter<String> pageFilter) {
    return Optional.ofNullable(pageFilter.getSortBy())
        .map(
            e -> {
              if (pageFilter.isSortDesc()) {
                e += " desc";
              }
              return e;
            })
        .orElse("createTime desc");
  }

  public PageFilter<T> doFilter() {
    return Optional.of(this)
        .map(
            e ->
                e.toBuilder()
                    .pageNumber(Math.max(e.getPageNumber(), 0))
                    .pageSize(Math.max(e.getPageSize(), 0))
                    .build())
        .orElse(null);
  }
}
