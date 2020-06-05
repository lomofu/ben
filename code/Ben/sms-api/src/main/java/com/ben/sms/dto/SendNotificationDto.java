package com.ben.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lomofu
 * @date 2020/3/24 21:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationDto implements Serializable {
  private static final long serialVersionUID = -6056051770547156865L;
  private String title;
}
