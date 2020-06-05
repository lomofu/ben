package com.ben.bot.dto.sms;

import com.ben.common.annotation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/8 20:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginWithPhoneCodeDto implements Serializable {
  private static final long serialVersionUID = 217431022802872873L;
  @PhoneNumber @NotBlank private String phoneNumber;
  @NotBlank private String code;
}
