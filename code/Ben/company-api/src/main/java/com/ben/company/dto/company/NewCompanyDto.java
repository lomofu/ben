package com.ben.company.dto.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

/**
 * @author lomofu
 * @date 2020/1/26 02:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewCompanyDto {
  @ApiModelProperty(value = "管理员账户id")
  @NotBlank(message = PARAM_MISSING)
  private String accountId;

  @ApiModelProperty(value = "公司/团队名称")
  @NotBlank(message = PARAM_MISSING)
  private String companyName;

  @ApiModelProperty(value = "类型: 0 为公司 1 为团队")
  private int type;

  @ApiModelProperty(value = "公司/团队: 地址")
  private String companyAddress;
}
