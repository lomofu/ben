package com.ben.bot.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/25 01:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JobDto implements Serializable {
  private static final long serialVersionUID = 9210784535510143546L;
  @NotBlank private String name;
  @NotBlank private String begin;
  @NotBlank private String end;
}
