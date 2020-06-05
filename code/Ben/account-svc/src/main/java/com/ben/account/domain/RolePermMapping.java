package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lomofu
 * @date 2020/3/15 17:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "role_perm_mapping")
public class RolePermMapping {
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @GeneratedValue(generator = "system-uuid")
  @Id
  private String id;

  @Column(name = "role_id", nullable = false)
  private String roleId;

  @Column(name = "perm_id", nullable = false)
  private String permId;
}
