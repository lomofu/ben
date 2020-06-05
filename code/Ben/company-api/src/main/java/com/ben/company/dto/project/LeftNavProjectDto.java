package com.ben.company.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lomofu
 * @date 2020/1/28 00:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LeftNavProjectDto {
  private String projectId;
  private String projectName;
  private String description;
  private Date createTime;
  private Date updateTime;
  @Builder.Default private String link = "/projects";
}
