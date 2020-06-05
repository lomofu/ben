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
 * @date 2020/3/25 01:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ScheduleDto implements Serializable {
  private static final long serialVersionUID = 8730950612747213016L;
  @NotBlank private String to;
  @NotEmpty private List<String> list;
}
