package com.ben.company.domain;

import com.ben.common.utils.UUIDHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "project")
public class Project implements Serializable {

  private static final long serialVersionUID = 6927665564434446808L;

  /** 项目id */
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** 项目名称 */
  private String name;

  /** 项目描述 */
  private String description;

  /** 项目所属团队id */
  @Column(name = "team_id")
  private String teamId;

  /** 创建时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 修改时间 */
  @Column(name = "update_time")
  private Date updateTime;

  /** 0为删除 1为未删除 */
  private Boolean active;
}
