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
 * @date 2020/3/24 22:52
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PushScheduleVO {
  @ApiModelProperty(value = "排班所属项目id")
  @NotBlank
  private String projectId;

  @ApiModelProperty(value = "排班推送方式 [email,phone]")
  @NotEmpty
  private List<String> selected;
}
