package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/22 19:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {
  private static final long serialVersionUID = 8105312210430301414L;
  @NotBlank private String to;
  @NotBlank private String title;
  @NotBlank private String content;
  @NotEmpty private List<String> list;
}
