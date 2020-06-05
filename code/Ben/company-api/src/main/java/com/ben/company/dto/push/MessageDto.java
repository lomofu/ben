package com.ben.company.dto.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lomofu
 * @date 2020/3/24 01:18
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
  private String id;
  private String title;
  private String content;
  private Date createTime;
  private String accountName;
  private String avatarUrl;
}
