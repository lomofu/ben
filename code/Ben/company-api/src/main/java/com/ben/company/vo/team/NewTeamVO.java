package com.ben.company.vo.team;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/1/26 23:23
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamVO {
  @ApiModelProperty(value = "创建者id")
  @NotBlank
  private String accountId;

  @ApiModelProperty(value = "公司id")
  @NotBlank
  private String companyId;

  @ApiModelProperty(value = "团队名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "团队描述")
  private String description;
}
