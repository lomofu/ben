package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/4 05:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewAdminDto implements Serializable {
  private static final long serialVersionUID = -2666107875744000439L;
  @NotBlank private String email;
  @NotBlank private String token;
}
