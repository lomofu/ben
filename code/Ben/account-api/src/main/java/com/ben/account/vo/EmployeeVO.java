package com.ben.account.vo;

import com.ben.common.annotation.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.CommonConstant.ILLEGAL_EMAIL;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

/**
 * @author lomofu
 * @date 2020/2/16 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EmployeeVO {
  @ApiModelProperty(value = "员工名称")
  @NotBlank(message = PARAM_MISSING)
  private String name;

  @ApiModelProperty(value = "员工性别", example = "3")
  private Integer gender;

  @ApiModelProperty(value = "员工邮箱")
  @Email(message = ILLEGAL_EMAIL)
  private String email;

  @ApiModelProperty(value = "员工手机")
  @PhoneNumber
  private String phoneNumber;

  @ApiModelProperty(value = "员工头像")
  private String avatarUrl;

  @ApiModelProperty(value = "员工密码")
  private String password;

  @ApiModelProperty(value = "员工确认密码")
  private String confirmPassword;

  @ApiModelProperty(value = "员工所属团队id")
  private String teamId;

  @ApiModelProperty(value = "员工所属公司id")
  @NotBlank(message = PARAM_MISSING)
  private String companyId;

  @ApiModelProperty(value = "员工是否为管理员")
  private boolean admin;
}
