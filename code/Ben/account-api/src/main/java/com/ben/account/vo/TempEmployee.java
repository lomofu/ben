package com.ben.account.vo;

import com.ben.common.annotation.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/2/15 22:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TempEmployee {
  @ApiModelProperty(value = "员工名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "员工邮箱")
  @NotBlank
  @Email
  private String email;

  @ApiModelProperty(value = "员工手机")
  @NotBlank
  @PhoneNumber
  private String phoneNumber;

  @ApiModelProperty(value = "员工性别")
  private int gender;

  @ApiModelProperty(value = "员工所属团队id")
  private String teamId;

  @ApiModelProperty(value = "所属公司id")
  @NotBlank
  private String companyId;

  @ApiModelProperty(value = "管理员id")
  @NotBlank
  private String adminId;

  @ApiModelProperty(value = "员工是否为admin")
  private boolean admin;
}
