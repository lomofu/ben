package com.ben.company.vo.team;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/1/27 19:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateTeamVO {
  @ApiModelProperty(value = "团队id")
  @NotBlank
  private String id;

  @ApiModelProperty(value = "团队名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "团队描述")
  private String description;

  @ApiModelProperty(value = "团队所属公司id")
  @NotBlank
  private String companyId;

  @ApiModelProperty(value = "操作人id")
  @NotBlank
  private String accountId;
}
