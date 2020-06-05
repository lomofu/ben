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
@Table(name = "team_mapping_account")
public class TeamMappingAccount implements Serializable {

  private static final long serialVersionUID = -6934311497574160688L;

  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** team id */
  @Column(name = "team_id")
  private String teamId;

  /** account id */
  @Column(name = "account_id")
  private String accountId;
}
