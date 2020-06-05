package com.ben.company.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private String id;
  private String name;
  private String begin;
  private String end;

}
