package com.ben.company.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/3/31 20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LineChartDto {
  private String date;
  @Builder.Default private long count = 0;
}
