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
@Table(name = "team")
public class Team implements Serializable {

  private static final long serialVersionUID = -7547643121468412243L;

  /** Team id */
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** Team 名字 */
  private String name;

  /** Team 描述 */
  private String description;

  /** 公司或团队id */
  @Column(name = "company_id")
  private String companyId;

  /** 创建时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 修改时间 */
  @Column(name = "update_time")
  private Date updateTime;

  /** 0为删除 1为未删除 */
  private Boolean active;
}
