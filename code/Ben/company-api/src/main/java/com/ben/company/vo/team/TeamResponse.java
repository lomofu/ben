package com.ben.company.vo.team;

import com.ben.common.response.BaseResponse;
import com.ben.company.enums.TeamEnum;
import lombok.*;

/**
 * @author lomofu
 * @date 2020/1/26 22:58
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TeamResponse extends BaseResponse {

  private static final long serialVersionUID = 3281228196569448529L;

  private Object data;

  public TeamResponse(TeamEnum teamEnum) {
    super.setMsg(teamEnum.getMessage());
  }

  public TeamResponse(TeamEnum teamEnum, Object data) {
    super.setMsg(teamEnum.getMessage());
    this.data = data;
  }
}
