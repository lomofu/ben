package com.ben.company.service.company.impl;

import com.ben.common.annotation.CacheExpire;
import com.ben.company.dao.CompanyMapper;
import com.ben.company.dao.CompanyMappingAccountMapper;
import com.ben.company.domain.Company;
import com.ben.company.domain.CompanyMappingAccount;
import com.ben.company.service.company.CompanyBaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Objects;

import static com.ben.common.constant.CommonConstant.DEFAULT_CACHE_TIME;
import static com.ben.company.constant.CommonConstant.*;
import static com.ben.company.constant.CompanyConstant.SELECT_COMPANY_ID;

/**
 * @author lomofu
 * @date 2020/2/28 15:17
 */
@Service
@CacheConfig(cacheNames = "company-svc")
public class CompanyBaseServiceImpl implements CompanyBaseService {

  @Resource private CompanyMapper companyMapper;

  @Resource private CompanyMappingAccountMapper companyMappingAccountMapper;

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName)",
      condition =
          "#token != null && #token.length()>0 && #companyId!=null && #companyId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public Company findCompanyById(String token, String companyId) {
    return companyMapper.selectOneByExample(
        Example.builder(Company.class)
            .where(Sqls.custom().andEqualTo(SELECT_ID, companyId).andEqualTo(SELECT_ACTIVE, true))
            .build());
  }

  @Override
  @Cacheable(
      key = "#token.concat(':').concat(#root.methodName)",
      condition =
          "#token != null && #token.length()>0 && #accountId!=null && #accountId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public Company findCompanyByAccountId(String token, String accountId) {
    Company company;
    company =
        companyMapper.selectOneByExample(
            Example.builder(Company.class)
                .where(
                    Sqls.custom()
                        .andEqualTo(SELECT_ACTIVE, true)
                        .andEqualTo(SELECT_ACCOUNT_ID, accountId))
                .build());
    if (Objects.isNull(company)) {
      company = companyMapper.selectCompanyByAccountId(accountId);
    }
    return company;
  }

  @Override
  @Cacheable(
      key = "#companyId.concat(':').concat(#root.methodName)",
      condition = "#companyId!=null && #companyId.length()>0",
      unless = "#result ==null")
  @CacheExpire(value = DEFAULT_CACHE_TIME)
  public int countMembersInCompanyByCompanyId(String token, String companyId) {
    return companyMappingAccountMapper.selectCountByExample(
        Example.builder(CompanyMappingAccount.class)
            .where(Sqls.custom().andEqualTo(SELECT_COMPANY_ID, companyId))
            .build());
  }
}
