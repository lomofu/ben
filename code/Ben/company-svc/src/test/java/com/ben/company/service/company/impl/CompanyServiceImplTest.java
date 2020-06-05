package com.ben.company.service.company.impl;

import com.ben.account.client.AccountClient;
import com.ben.common.utils.RedissonDistributedLocker;
import com.ben.company.config.AppConfiguration;
import com.ben.company.dao.CompanyMapper;
import com.ben.company.dao.CompanyMappingAccountMapper;
import com.ben.company.domain.Company;
import com.ben.company.service.company.CompanyBaseService;
import com.ben.company.service.job.JobBaseService;
import com.ben.company.service.project.ProjectBaseService;
import com.ben.company.service.team.TeamBaseService;
import com.ben.company.vo.company.CompanyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyServiceImplTest {
  public static final String COMPANY_ID = "a8b407eb724749a3bfb220b3898ff1e9";

  @Tested private CompanyServiceImpl companyService;

  @Injectable private AccountClient accountClient;

  @Injectable private CompanyMapper companyMapper;

  @Injectable private CompanyMappingAccountMapper companyMappingAccountMapper;

  @Injectable private CompanyBaseService companyBaseService;

  @Injectable private TeamBaseService teamBaseService;

  @Injectable private ProjectBaseService projectBaseService;

  @Injectable private JobBaseService jobBaseService;

  @Injectable private ObjectMapper objectMapper;

  @Injectable private RedissonDistributedLocker locker;

  @Injectable private AppConfiguration appConfiguration;

  @Test
  public void should_return_success_when_get_company_by_id() {
    Assertions.assertNotNull(companyBaseService);
    new Expectations() {
      {
        companyBaseService.findCompanyById(anyString, anyString);
        result = new Company();
      }
    };
    CompanyResponse response = companyService.getCompanyById("token", COMPANY_ID);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getCode(), 200);
  }
}
