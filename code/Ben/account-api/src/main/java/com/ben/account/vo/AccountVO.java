package com.ben.account.vo;

import com.ben.common.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

import static com.ben.common.constant.CommonConstant.ILLEGAL_EMAIL;
import static com.ben.common.constant.CommonConstant.PARAM_MISSING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccountVO {
  private String id;

  @NotBlank(message = PARAM_MISSING)
  private String name;

  private Integer gender;

  @Email(message = ILLEGAL_EMAIL)
  private String email;

  @PhoneNumber private String phoneNumber;
  private String avatarUrl;
  private Date createTime;
}
