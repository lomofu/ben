package com.ben.gateway.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lomofu
 * @date 2020/3/1 02:32
 */
@Getter
@Setter
public class SkipDto {
  private String method;
  private String url;
}
