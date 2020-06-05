package com.ben.company.service.company;

import com.ben.company.dto.company.NewCompanyDto;
import com.ben.company.vo.company.CompanyResponse;
import com.ben.company.vo.company.UpdateCompanyVO;

/**
 * @author lomofu
 * @date 2020/1/24 14:23
 */
public interface CompanyService {
  CompanyResponse getCompanyById(String token, String id);

  CompanyResponse getCompanyByIdForFeign(String token, String id);

  CompanyResponse getAccountAndCompanyByToken(String token);

  CompanyResponse createNewCompany(NewCompanyDto newCompanyDto);

  CompanyResponse updateCompany(String token, UpdateCompanyVO updateCompanyVO);

  CompanyResponse getTotalCountByCompanyId(String token, String companyId);

  CompanyResponse createEmployeeMappingWithCompany(String accountId, String companyId);

  CompanyResponse createEmployeeMappingWithTeam(String accountId, String teamId);

  CompanyResponse deleteAccountMapping(String token, String id);
}
