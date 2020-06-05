package com.ben.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lomofu
 * @date 2020/3/3 19:55
 */
@Data
@Builder
@AllArgsConstructor
public class GatewayEntry implements Serializable {
  private static final long serialVersionUID = -2118885970517219786L;
  private String uuid;
  private Date createTime;
}
