package com.ben.company.service.company;

import com.ben.company.domain.Company;

/**
 * @author lomofu
 * @date 2020/2/28 15:16
 */
public interface CompanyBaseService {
  Company findCompanyById(String token, String companyId);

  Company findCompanyByAccountId(String token, String accountId);

  int countMembersInCompanyByCompanyId(String token,String companyId);
}
