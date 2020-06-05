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
 * @date 2020/3/21 16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateAccountRoleVO {
  @ApiModelProperty(value = "角色id")
  @NotBlank(message = PARAM_MISSING)
  private String roleId;

  @ApiModelProperty(value = "账户id")
  @NotBlank(message = PARAM_MISSING)
  private String accountId;
}
