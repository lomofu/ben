package com.ben.company.vo.team;

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
 * @date 2020/2/20 21:53
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountListToTeamMappingVO<T> {
  @ApiModelProperty(value = "团队id")
  @NotBlank
  private String teamId;

  @ApiModelProperty(value = "账户列表")
  @NotEmpty
  private List<T> list;
}
