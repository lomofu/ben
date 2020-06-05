package com.ben.company.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/1/27 20:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewProjectVO {
  @ApiModelProperty(value = "项目名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "项目描述")
  private String description;

  @ApiModelProperty(value = "项目所属团队id")
  @NotBlank
  private String teamId;

  @ApiModelProperty(value = "操作人id")
  @NotBlank
  private String accountId;
}
