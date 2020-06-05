package com.ben.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/3/20 00:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RoleDto {
  private String id;
  private String name;
  private String description;
  private String createBy;
  private boolean active;
  private String createName;
}
