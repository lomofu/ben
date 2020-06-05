package com.ben.company.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/1/24 15:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompanyDto {
  private String id;
  private String name;
  @Builder.Default private String description = null;
  @Builder.Default private String address = null;
  private int type;
  private String accountId;
  private String accountName;
  private String email;
  private int gender;
  private String phoneNumber;
  private String avatarUrl;
  private Date createTime;
  private Date companyCreateTime;
  private Date companyUpdateTime;
  private boolean admin;
  private boolean active;
  private List<String> role;
  private List<String> permissionList;
}
