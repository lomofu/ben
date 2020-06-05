package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lomofu
 * @date 2020/3/15 16:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "account_role_mapping")
public class AccountRoleMapping {
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @GeneratedValue(generator = "system-uuid")
  @Id
  private String id;

  @Column(name = "account_id", nullable = false)
  private String accountId;

  @Column(name = "role_id", nullable = false)
  private String roleId;
}
