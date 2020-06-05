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
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "message")
public class Message {
  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  private String title;

  private String content;

  /** 创建公告时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 创建者id */
  @Column(name = "create_by")
  private String createBy;
}
