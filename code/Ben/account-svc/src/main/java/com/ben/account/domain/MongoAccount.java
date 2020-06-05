package com.ben.account.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "accounts")
public class MongoAccount {
  @Id private String id;
  private String email;
  private String name;
  private Integer gender;
  private String phoneNumber;
  private String password;
  private String confirmPassword;
  private Integer type;
  private String companyName;
  private String companyAddress;
}
