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
 * @date 2020/2/4 23:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateVO {
  @ApiModelProperty(value = "账户id")
  @NotBlank(message = PARAM_MISSING)
  private String id;

  @ApiModelProperty(value = "账户名称")
  @NotBlank(message = PARAM_MISSING)
  private String name;

  @ApiModelProperty(value = "账户性别", example = "3")
  private Integer gender;

  @ApiModelProperty(value = "账户邮箱")
  @Email(message = ILLEGAL_EMAIL)
  private String email;

  @ApiModelProperty(value = "账户手机")
  @PhoneNumber
  private String phoneNumber;
}
