package com.ben.company.enums;

import com.ben.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ben.common.enums.ResultCode.*;

/**
 * AccountEnum Enum
 *
 * @author lomofu
 */
@Getter
@AllArgsConstructor
public enum CompanyEnum {
  COMPANY_IS_EMPTY("公司不存在", NOT_FOUND),

  COMPANY_CREATED_SUCCESS("公司创建成功", SUCCESS),

  COMPANY_CREATED_FAIL("公司创建失败", FAILURE),

  COMPANY_UPDATE_SUCCESS("公司信息更新成功", SUCCESS),

  COMPANY_UPDATE_FAIL("公司信息更新失败", FAILURE),

  WITHOUT_RES("没有结果", NOT_FOUND),

  MAPPING_SUCCESS("映射成功", SUCCESS),

  MAPPING_DELETE_SUCCESS("映射删除成功", SUCCESS),

  MAPPING_FAIL("映射失败", FAILURE),

  MAPPING_DELETE_FAIL("映射删除失败", FAILURE),

  ACCOUNT_DELETE_FAIL("账户删除失败", FAILURE);

  final String message;

  final ResultCode resultCode;
}
