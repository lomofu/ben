package com.ben.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lomofu
 * @date 2020/2/10 22:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {
  private int pages;
  private long total;
  private List<T> list;
  private boolean hasNextPage;
}
