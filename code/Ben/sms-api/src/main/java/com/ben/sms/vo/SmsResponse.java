package com.ben.sms.vo;

import com.ben.common.response.BaseResponse;
import com.ben.sms.enums.SmsEnum;
import lombok.*;

/**
 * @author lomofu
 * @date 2020/3/9 00:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SmsResponse extends BaseResponse {
  private static final long serialVersionUID = -3328571800868260108L;

  private Object data;

  public SmsResponse(SmsEnum smsEnum) {
    super.setMsg(smsEnum.getMessage());
  }

  public SmsResponse(SmsEnum smsEnum, Object data) {
    super.setMsg(smsEnum.getMessage());
    this.data = data;
  }
}
