package com.ben.company.vo.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/27 23:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LeftNavTeamVO {
  private String teamId;
  private String teamName;
  private String description;
  private Date createTime;
  private Date updateTime;
  @Builder.Default private String link = null;
  private List<Object> children;
}
