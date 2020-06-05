package com.ben.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/2/21 21:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SimpleAccountDto {
  private String id;
  private String name;
  private String avatarUrl;
  private boolean isAdmin;
}
