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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "job_mapping_account")
public class JobMappingAccount implements Serializable {

  private static final long serialVersionUID = -8864412690224499628L;

  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** job id */
  @Column(name = "job_id")
  private String jobId;

  /** account id */
  @Column(name = "account_id")
  private String accountId;
}
