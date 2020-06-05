package com.ben.company.vo.project;

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
 * @date 2020/2/22 00:22
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountListToProjectMappingVO<T> {
  @ApiModelProperty(value = "项目id")
  @NotBlank
  private String projectId;

  @ApiModelProperty(value = "账户列表")
  @NotEmpty
  private List<T> list;
}
