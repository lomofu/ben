package com.ben.company.vo.job;

import com.ben.common.response.BaseResponse;
import com.ben.company.enums.JobEnum;
import lombok.*;

/**
 * @author lomofu
 * @date 2020/2/14 15:20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JobResponse extends BaseResponse {
  private Object data;

  public JobResponse(JobEnum jobEnum) {
    super.setMsg(jobEnum.getMessage());
  }

  public JobResponse(JobEnum jobEnum, Object data) {
    super.setMsg(jobEnum.getMessage());
    this.data = data;
  }
}
