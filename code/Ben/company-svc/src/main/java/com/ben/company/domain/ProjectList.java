package com.ben.company.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lomofu
 * @date 2020/2/10 21:43
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectList implements Serializable {

  private static final long serialVersionUID = 1966668935983035873L;

  private String id;
  private String name;
  private String description;
  private String teamId;
  private Date createTime;
  private Date updateTime;
  private Boolean active;
  private String teamName;
}
