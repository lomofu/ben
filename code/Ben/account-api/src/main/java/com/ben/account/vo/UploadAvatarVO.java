package com.ben.account.vo;

import com.ben.common.annotation.PhoneNumber;
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
 * @date 2020/3/10 18:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UploadAvatarVO {
  @NotBlank(message = PARAM_MISSING)
  private String id;

  @NotBlank(message = PARAM_MISSING)
  private String name;

  @Email(message = ILLEGAL_EMAIL)
  private String email;

  @PhoneNumber private String phoneNumber;
}
