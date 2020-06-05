package com.ben.company.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/1/27 20:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateProjectVO {
  @ApiModelProperty(value = "项目id")
  @NotBlank
  private String id;

  @ApiModelProperty(value = "项目名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "项目描述")
  private String description;

  @ApiModelProperty(value = "团队id")
  @NotBlank
  private String teamId;
}
