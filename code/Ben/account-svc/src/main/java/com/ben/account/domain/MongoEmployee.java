package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author lomofu
 * @date 2020/1/23 17:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "employees")
public class MongoEmployee {
  @Id private String id;
  private String email;
  private String name;
  private Integer gender;
  private String phoneNumber;
  private Date createTime;
  private String avatarUrl;
  private String companyId;
  private String teamId;
  private boolean active;
  private boolean admin;
  private String token;
  private Date expireAt;
}
