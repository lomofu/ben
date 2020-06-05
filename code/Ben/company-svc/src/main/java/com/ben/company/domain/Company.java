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
@Table(name = "company")
public class Company implements Serializable {

  private static final long serialVersionUID = 3870999100687116068L;

  /** 公司或团队id */
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** 公司或团队名称 */
  private String name;

  /** 0为团队 1为公司 */
  private Integer type;

  /** 公司或团队地址 */
  private String address;

  /** 公司或团队简介 */
  private String description;

  /** 公司或团队管理员id */
  @Column(name = "account_id")
  private String accountId;

  /** 创建时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 修改时间 */
  @Column(name = "update_time")
  private Date updateTime;

  /** 0为删除 1为未删除 */
  private Boolean active;
}
