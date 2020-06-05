package com.ben.company.vo.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/2/24 21:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class JobVO {
  private String id;
  private String name;
  private String description;
  private String start;
  private String end;
  private String color;
  private boolean full;
  private boolean publish;
  private String accountId;
  private String projectId;
}
