package com.ben.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lomofu
 * @date 2020/3/21 15:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SimpleRoleDto {
  private String id;
  private String name;
}
