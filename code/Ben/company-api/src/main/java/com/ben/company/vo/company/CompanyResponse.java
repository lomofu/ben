package com.ben.company.vo.company;

import com.ben.common.response.BaseResponse;
import com.ben.company.enums.CompanyEnum;
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
public class CompanyResponse extends BaseResponse {
  private Object data;

  public CompanyResponse(CompanyEnum companyEnum) {
    super.setMsg(companyEnum.getMessage());
  }

  public CompanyResponse(CompanyEnum companyEnum, Object data) {
    super.setMsg(companyEnum.getMessage());
    this.data = data;
  }
}
