package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "account")
public class Account implements Serializable {

  private static final long serialVersionUID = -7961780700648631192L;

  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @GeneratedValue(generator = "system-uuid")
  @Id
  private String id;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "gender", nullable = false, length = 1)
  private Integer gender;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "phone_number", nullable = false, length = 11)
  private String phoneNumber;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "avatar_url", nullable = false)
  private String avatarUrl;

  @Column(name = "create_time", nullable = false)
  private Date createTime;

  @Column(name = "is_active")
  private boolean active;

  @Column(name = "is_admin")
  private boolean admin;
}
