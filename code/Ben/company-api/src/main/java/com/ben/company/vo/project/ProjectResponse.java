package com.ben.company.vo.project;

import com.ben.common.response.BaseResponse;
import com.ben.company.enums.ProjectEnum;
import lombok.*;

/**
 * AccountResponse class
 *
 * @author lomofu
 * @date 2020/01/15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjectResponse extends BaseResponse {
  private Object data;

  public ProjectResponse(ProjectEnum projectEnum) {
    super.setMsg(projectEnum.getMessage());
  }

  public ProjectResponse(ProjectEnum projectEnum, Object data) {
    super.setMsg(projectEnum.getMessage());
    this.data = data;
  }
}
