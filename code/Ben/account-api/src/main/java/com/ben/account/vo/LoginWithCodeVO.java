package com.ben.account.vo;

import com.ben.common.annotation.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/3/9 20:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginWithCodeVO {
  @ApiModelProperty(value = "手机号码")
  @PhoneNumber
  @NotBlank
  private String phoneNumber;

  @ApiModelProperty(value = "短信验证码")
  @NotBlank
  private String code;

  @ApiModelProperty(value = "是否记住我")
  private boolean isRememberMe;
}
