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
@Table(name = "project_mapping_account")
public class ProjectMappingAccount implements Serializable {

  private static final long serialVersionUID = -3306785036455194601L;

  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** project id */
  @Column(name = "project_id")
  private String projectId;

  /** account id */
  @Column(name = "`account_id`")
  private String accountId;
}
