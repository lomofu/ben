package com.ben.company.vo.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lomofu
 * @date 2020/2/14 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateJobVO {
  @NotBlank private String id;
  @NotBlank private String name;
  private String description;
  @NotNull private String start;
  @NotNull private String end;
  @NotBlank private String color;
  private boolean full;
  @NotBlank private String accountId;
  @NotBlank private String projectId;
}
