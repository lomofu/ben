package com.ben.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/26 02:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PermissionDto implements Serializable {
  private static final long serialVersionUID = 8413178045658253635L;
  private String id;
  private String perm;
}
