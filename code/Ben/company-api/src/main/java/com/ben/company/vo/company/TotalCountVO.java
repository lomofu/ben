package com.ben.company.vo.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/1/29 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TotalCountVO {
  private int projectCount;
  private int teamCount;
  private int memberCount;
}
