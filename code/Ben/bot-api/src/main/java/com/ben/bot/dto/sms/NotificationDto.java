package com.ben.bot.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/24 21:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {
  private static final long serialVersionUID = -2475335227523504327L;
  @NotBlank private String title;
  @NotBlank private List<String> list;
}
