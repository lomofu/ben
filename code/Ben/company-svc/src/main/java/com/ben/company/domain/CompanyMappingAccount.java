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
@Table(name = "company_mapping_account")
public class CompanyMappingAccount implements Serializable {

  private static final long serialVersionUID = -7666609917101459742L;

  @Id
  @KeySql(genId = UUIDHelper.class)
  private String id;

  /** 公司id */
  @Column(name = "company_id")
  private String companyId;

  /** 用户id */
  @Column(name = "account_id")
  private String accountId;
}
