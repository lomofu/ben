package com.ben.company.dao;

import com.ben.company.domain.Company;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface CompanyMapper extends Mapper<Company> {
  @Select(
      value =
          "SELECT c.* FROM company c LEFT JOIN company_mapping_account m ON c.id  = m.company_id "
              + "WHERE m.account_id = #{accountId,jdbcType=VARCHAR} AND c.active = 1")
  Company selectCompanyByAccountId(@Param("accountId") String accountId);
}
