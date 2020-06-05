package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/4 22:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewEmployeeSuccessDto implements Serializable {
  private static final long serialVersionUID = -4132123798512986886L;
  // 管理员邮箱
  @NotBlank private String email;
  // 雇员名称
  @NotBlank private String name;
}
