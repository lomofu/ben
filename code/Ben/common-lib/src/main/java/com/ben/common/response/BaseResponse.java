package com.ben.common.response;

import com.ben.common.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * BaseResponse class
 *
 * @author lomofu
 * @date 2020/01/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class BaseResponse implements Serializable {
  private static final long serialVersionUID = -8060071541048736274L;

  @ApiModelProperty(value = "http状态码")
  @Builder.Default
  private int code = ResultCode.SUCCESS.getCode();

  @ApiModelProperty(value = "http响应信息")
  @Builder.Default
  private String msg = ResultCode.SUCCESS.getMessage();
}
