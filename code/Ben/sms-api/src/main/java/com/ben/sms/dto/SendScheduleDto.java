package com.ben.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/25 16:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendScheduleDto implements Serializable {
  private static final long serialVersionUID = -7618391419802270423L;
  private String shcedule;
}
