package com.ben.company.vo.push;

import com.ben.common.response.BaseResponse;
import com.ben.company.enums.PushEnum;
import lombok.*;

/**
 * @author lomofu
 * @date 2020/3/22 14:38
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PushResponse extends BaseResponse {
  private static final long serialVersionUID = -786039665295715715L;
  private Object data;

  public PushResponse(PushEnum pushEnum) {
    super.setMsg(pushEnum.getMessage());
  }

  public PushResponse(PushEnum pushEnum, Object data) {
    super.setMsg(pushEnum.getMessage());
    this.data = data;
  }
}
