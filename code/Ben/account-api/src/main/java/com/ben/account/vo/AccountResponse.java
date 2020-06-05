package com.ben.account.vo;

import com.ben.account.enums.AccountEnum;
import com.ben.account.enums.RoleEnum;
import com.ben.common.response.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

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
@ApiModel
public class AccountResponse extends BaseResponse implements Serializable {

  private static final long serialVersionUID = -7279694648816606289L;

  @ApiModelProperty(value = "返回的数据")
  private Object data;

  public AccountResponse(AccountEnum accountEnum) {
    super.setMsg(accountEnum.getMessage());
    this.data = accountEnum.getMessage();
  }

  public AccountResponse(RoleEnum roleEnum) {
    super.setMsg(roleEnum.getMessage());
    this.data = roleEnum.getMessage();
  }

  public AccountResponse(AccountEnum accountEnum, Object data) {
    super.setMsg(accountEnum.getMessage());
    this.data = data;
  }
}
