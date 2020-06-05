package com.ben.account.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

/**
 * @author lomofu
 * @date 2020/3/20 21:20
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleVO {
  @ApiModelProperty(value = "角色id")
  @NotBlank(message = PARAM_MISSING)
  private String id;

  @ApiModelProperty(value = "角色名称")
  @NotBlank(message = PARAM_MISSING)
  private String name;

  @ApiModelProperty(value = "角色描述")
  private String description;

  @ApiModelProperty(value = "角色创建者id")
  @NotBlank(message = PARAM_MISSING)
  private String createBy;

  @ApiModelProperty(value = "角色所属公司id")
  @NotBlank(message = PARAM_MISSING)
  private String companyId;
}
