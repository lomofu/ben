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
 * @date 2020/3/25 17:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailScheduleDto implements Serializable {
  private static final long serialVersionUID = -7360519503523941241L;
  @NotBlank private String to;
  @NotEmpty private List<JobDto> list;
}
