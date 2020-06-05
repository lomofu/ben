package com.ben.bot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lomofu
 * @date 2020/3/22 02:13
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationVO {
  @ApiModelProperty(value = "公告标题")
  @NotBlank
  private String title;

  @ApiModelProperty(value = "公告内容")
  @NotBlank
  private String content;

  @ApiModelProperty(value = "公告发送者id")
  @NotBlank
  private String createBy;

  @ApiModelProperty(value = "所属公司id")
  @NotBlank
  private String companyId;

  @ApiModelProperty(value = "推送方式 [app,email,phone]")
  @NotEmpty
  private List<String> selected;
}
