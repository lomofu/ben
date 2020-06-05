package com.ben.bot.vo;

import com.ben.bot.enums.BotEnum;
import com.ben.common.response.BaseResponse;
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
public class BotResponse extends BaseResponse {
  private static final long serialVersionUID = -8076145025200055852L;

  private Object data;

  public BotResponse(BotEnum botEnum) {
    super.setMsg(botEnum.getMessage());
  }

  public BotResponse(BotEnum companyEnum, Object data) {
    super.setMsg(companyEnum.getMessage());
    this.data = data;
  }
}
