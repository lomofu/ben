package com.ben.company.vo.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/2/9 01:19
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTeamListVO {
  private String id;
  private String name;
}
