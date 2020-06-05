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
@Table(name = "job")
public class Job implements Serializable {

  private static final long serialVersionUID = -3191702657222416402L;

  /** 任务id */
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** 任务名称 */
  private String name;

  /** 任务描述 */
  private String description;

  /** 任务开始时间 */
  private Date start;

  /** 任务结束时间 */
  private Date end;

  /** 任务颜色 */
  private String color;

  /** 0为精准时间,1为全天 */
  private Boolean full;

  /** 任务所属项目id */
  @Column(name = "project_id")
  private String projectId;

  /** 0 未发布 1 发布 */
  private Boolean publish;

  /** 0为删除 1为未删除 */
  private Boolean active;

  /** 任务owner id */
  @Column(name = "account_id")
  private String accountId;
}
