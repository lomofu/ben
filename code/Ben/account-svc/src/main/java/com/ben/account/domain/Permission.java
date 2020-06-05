package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author lomofu
 * @date 2020/3/15 16:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "permission")
public class Permission {
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @GeneratedValue(generator = "system-uuid")
  @Id
  private String id;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "description", length = 100)
  private String description;

  @Column(name = "perm", nullable = false)
  private String perm;

  private String url;

  private String icon;
}
