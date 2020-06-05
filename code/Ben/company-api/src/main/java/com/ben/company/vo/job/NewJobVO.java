package com.ben.company.vo.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/2/14 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NewJobVO {
  @ApiModelProperty(value = "任务名称")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "任务描述")
  private String description;

  @ApiModelProperty(value = "任务开始时间")
  @NotBlank
  private String start;

  @ApiModelProperty(value = "任务结束时间")
  @NotBlank
  private String end;

  @ApiModelProperty(value = "任务颜色")
  @NotBlank
  private String color;

  @ApiModelProperty(value = "是否为全天")
  private boolean full;

  @ApiModelProperty(value = "任务所属账户id")
  @NotBlank
  private String accountId;

  @ApiModelProperty(value = "任务所属项目id")
  @NotBlank
  private String projectId;
}
