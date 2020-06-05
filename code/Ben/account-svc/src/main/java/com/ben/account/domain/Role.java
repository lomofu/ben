package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lomofu
 * @date 2020/3/15 16:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "role")
public class Role {
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @GeneratedValue(generator = "system-uuid")
  @Id
  private String id;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "description", length = 100)
  private String description;

  @Column(name = "create_by", nullable = false)
  private String createBy;

  private boolean active;
}
