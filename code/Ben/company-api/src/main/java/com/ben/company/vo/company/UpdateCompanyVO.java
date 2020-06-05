package com.ben.company.vo.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/1/27 18:53
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyVO {
  @ApiModelProperty(value = "公司id")
  @NotBlank
  private String id;

  @ApiModelProperty(value = "公司名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "公司简述")
  private String description;

  @ApiModelProperty(value = "公司地址")
  private String address;

  @ApiModelProperty(value = "操作人id")
  private String accountId;
}
