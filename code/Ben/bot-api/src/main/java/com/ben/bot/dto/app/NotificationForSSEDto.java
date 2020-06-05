package com.ben.bot.dto.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/23 18:41
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationForSSEDto implements Serializable {
  private static final long serialVersionUID = -8805381205988477911L;
  @NotBlank private String to;
  @NotBlank private String title;
  @NotBlank private String content;
  @NotBlank private String companyId;
}
