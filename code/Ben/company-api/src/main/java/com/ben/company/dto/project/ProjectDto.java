package com.ben.company.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lomofu
 * @date 2020/2/10 21:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProjectDto {
  private String id;
  private String name;
  private String description;
  private Date createTime;
  private Date updateTime;
  private boolean active;
  private String teamId;
  private String teamName;
  private int total;
}
