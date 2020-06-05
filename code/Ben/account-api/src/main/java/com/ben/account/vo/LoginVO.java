package com.ben.account.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

@Data
@NoArgsConstructor
public class LoginVO {
  @ApiModelProperty(value = "账户: 邮箱或手机")
  @NotBlank(message = PARAM_MISSING)
  private String account;

  @ApiModelProperty(value = "密码")
  @NotBlank(message = PARAM_MISSING)
  private String password;

  @ApiModelProperty(value = "是否记住我")
  private boolean isRememberMe;
}
