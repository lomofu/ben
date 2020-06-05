package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/4 23:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewAdminSuccessDto implements Serializable {
  private static final long serialVersionUID = 5434249795277456707L;
  @NotBlank private String email;
  @NotBlank private String name;
}
