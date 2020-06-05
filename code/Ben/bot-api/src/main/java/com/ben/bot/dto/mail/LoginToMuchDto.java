package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/5 21:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginToMuchDto implements Serializable {
  private static final long serialVersionUID = -7796020829708085994L;
  // 管理员邮箱
  @NotBlank private String email;
  // 账户登陆方式（邮箱,电话号码)
  @NotBlank private String account;
}
