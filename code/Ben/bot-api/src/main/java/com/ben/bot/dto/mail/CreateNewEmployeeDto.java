package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/4 21:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewEmployeeDto implements Serializable {
  private static final long serialVersionUID = 7833854506381178526L;
  // 管理员名称
  @NotBlank private String from;
  // 雇员邮箱
  @NotBlank private String to;
  @NotBlank private String companyName;
  @NotBlank private String token;
}
