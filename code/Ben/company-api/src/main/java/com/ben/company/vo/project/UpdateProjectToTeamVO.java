package com.ben.company.vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author lomofu
 * @date 2020/2/11 22:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Builder(toBuilder = true)
public class UpdateProjectToTeamVO {
  @ApiModelProperty(value = "项目id")
  @NotBlank
  private String id;

  @ApiModelProperty(value = "团队id")
  @NotBlank
  private String teamId;
}
